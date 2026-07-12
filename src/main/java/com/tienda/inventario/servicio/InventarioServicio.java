package com.tienda.inventario.servicio;

import com.tienda.inventario.modelo.*;
import com.tienda.inventario.patron.comando.RegistrarMovimiento;
import com.tienda.inventario.patron.estrategia.EstrategiaTransferencia;
import com.tienda.inventario.patron.observador.ObservadorStock;
import com.tienda.inventario.repositorio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class InventarioServicio {

    private final ProductoRepositorio productoRepo;
    private final AlmacenRepositorio almacenRepo;
    private final StockRepositorio stockRepo;
    private final MovimientoRepositorio movimientoRepo;
    private final RedisTemplate<String, String> redisTemplate;
    private final List<ObservadorStock> observadores;
    private final Map<String, EstrategiaTransferencia> estrategias;

    // ==================== PRODUCTOS ====================

    @Transactional
    public Producto registrarProducto(Producto producto) {
        if (productoRepo.existsByCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un producto con código: " + producto.getCodigo());
        }
        return productoRepo.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = obtenerProductoPorId(id);
        producto.setCodigo(productoActualizado.getCodigo());
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setStockMinimo(productoActualizado.getStockMinimo());
        return productoRepo.save(producto);
    }

    // ==================== ALMACENES ====================

    @Transactional
    public Almacen registrarAlmacen(Almacen almacen) {
        if (almacenRepo.existsByCodigo(almacen.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un almacén con código: " + almacen.getCodigo());
        }
        return almacenRepo.save(almacen);
    }

    public List<Almacen> listarAlmacenes() {
        return almacenRepo.findAll();
    }

    // ==================== STOCK ====================

    @Transactional
    public Stock actualizarStock(Long productoId, Long almacenId, Integer cantidad, String notas) {
        Producto producto = productoRepo.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        Almacen almacen = almacenRepo.findById(almacenId)
                .orElseThrow(() -> new IllegalArgumentException("Almacén no encontrado"));

        Stock stock = stockRepo.findByProductoIdAndAlmacenId(productoId, almacenId)
                .orElse(Stock.builder()
                        .producto(producto)
                        .almacen(almacen)
                        .cantidadReservada(0)
                        .build());

        int cantidadAnterior = stock.getCantidad() != null ? stock.getCantidad() : 0;
        int diferencia = cantidad - cantidadAnterior;
        stock.setCantidad(cantidad);

        Stock guardado = stockRepo.save(stock);

        // Registrar el movimiento usando el patrón Command
        Movimiento.TipoMovimiento tipo = diferencia >= 0
                ? Movimiento.TipoMovimiento.ENTRADA
                : Movimiento.TipoMovimiento.SALIDA;

        new RegistrarMovimiento(movimientoRepo, producto, null, almacen,
                tipo, Math.abs(diferencia), null, notas).ejecutar();

        // Notificar a los observadores (patrón Observer)
        notificarObservadores(guardado, cantidadAnterior);

        return guardado;
    }

    public List<Stock> verStockPorProducto(Long productoId) {
        return stockRepo.findByProductoId(productoId);
    }

    public List<Stock> verStockPorAlmacen(Long almacenId) {
        return stockRepo.findByAlmacenId(almacenId);
    }

    // ==================== RESERVAS (REDIS) ====================

    @Transactional
    public String reservarStock(Long productoId, Long almacenId, Integer cantidad, String referencia) {
        Stock stock = stockRepo.buscarConBloqueo(productoId, almacenId)
                .orElseThrow(() -> new IllegalArgumentException("No hay stock para ese producto/almacén"));

        if (stock.getCantidadDisponible() < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Disponible: "
                    + stock.getCantidadDisponible() + ", Solicitado: " + cantidad);
        }

        stock.setCantidadReservada(stock.getCantidadReservada() + cantidad);
        stockRepo.save(stock);

        // Guardar reserva en Redis con vencimiento de 30 minutos
        String clave = "reserva:" + referencia;
        String valor = productoId + ":" + almacenId + ":" + cantidad;
        redisTemplate.opsForValue().set(clave, valor, 30, TimeUnit.MINUTES);

        new RegistrarMovimiento(movimientoRepo, stock.getProducto(), null, stock.getAlmacen(),
                Movimiento.TipoMovimiento.RESERVA, cantidad, referencia, "Reserva creada").ejecutar();

        return "Reserva creada: " + referencia;
    }

    @Transactional
    public String cancelarReserva(String referencia) {
        String clave = "reserva:" + referencia;
        String valor = redisTemplate.opsForValue().get(clave);

        if (valor == null) {
            throw new IllegalArgumentException("Reserva no encontrada o expirada: " + referencia);
        }

        String[] partes = valor.split(":");
        Long productoId = Long.parseLong(partes[0]);
        Long almacenId = Long.parseLong(partes[1]);
        int cantidad = Integer.parseInt(partes[2]);

        Stock stock = stockRepo.buscarConBloqueo(productoId, almacenId)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado"));

        stock.setCantidadReservada(Math.max(0, stock.getCantidadReservada() - cantidad));
        stockRepo.save(stock);
        redisTemplate.delete(clave);

        new RegistrarMovimiento(movimientoRepo, stock.getProducto(), stock.getAlmacen(), null,
                Movimiento.TipoMovimiento.CANCELAR_RESERVA, cantidad, referencia, "Reserva cancelada").ejecutar();

        return "Reserva cancelada: " + referencia;
    }

    // ==================== TRANSFERENCIAS ====================

    @Transactional
    public String transferirStock(Long productoId, Long origenId, Long destinoId,
                                  Integer cantidad, String tipoEstrategia) {
        Stock stockOrigen = stockRepo.buscarConBloqueo(productoId, origenId)
                .orElseThrow(() -> new IllegalArgumentException("Stock origen no encontrado"));

        // Seleccionar estrategia de transferencia (patrón Strategy)
        String nombreEstrategia = tipoEstrategia != null
                ? tipoEstrategia.toLowerCase() + "Transferencia"
                : "transferenciaEstandar";

        EstrategiaTransferencia estrategia = estrategias.getOrDefault(
                nombreEstrategia, estrategias.get("transferenciaEstandar"));

        if (!estrategia.puedeTransferir(stockOrigen, cantidad)) {
            throw new IllegalStateException("Transferencia no permitida por estrategia '"
                    + estrategia.getNombre() + "'. Disponible: " + stockOrigen.getCantidadDisponible());
        }

        stockOrigen.setCantidad(stockOrigen.getCantidad() - cantidad);
        stockRepo.save(stockOrigen);

        Almacen destino = almacenRepo.findById(destinoId)
                .orElseThrow(() -> new IllegalArgumentException("Almacén destino no encontrado"));

        Stock stockDestino = stockRepo.findByProductoIdAndAlmacenId(productoId, destinoId)
                .orElse(Stock.builder()
                        .producto(stockOrigen.getProducto())
                        .almacen(destino)
                        .cantidad(0)
                        .cantidadReservada(0)
                        .build());

        stockDestino.setCantidad(stockDestino.getCantidad() + cantidad);
        stockRepo.save(stockDestino);

        new RegistrarMovimiento(movimientoRepo, stockOrigen.getProducto(),
                stockOrigen.getAlmacen(), destino,
                Movimiento.TipoMovimiento.TRANSFERENCIA, cantidad, null, null).ejecutar();

        notificarObservadores(stockOrigen, stockOrigen.getCantidad() + cantidad);

        return "Transferencia exitosa: " + cantidad + " unidades de "
                + stockOrigen.getProducto().getNombre();
    }

    // ==================== ALERTAS Y REPORTES ====================

    public List<Stock> verAlertasStockBajo() {
        return stockRepo.buscarStockBajo();
    }

    public List<Movimiento> generarReporte(LocalDateTime desde, LocalDateTime hasta) {
        return movimientoRepo.buscarPorFechas(desde, hasta);
    }

    // Notifica a todos los observadores registrados
    private void notificarObservadores(Stock stock, int cantidadAnterior) {
        observadores.forEach(obs -> obs.alCambiarStock(stock, cantidadAnterior));
    }
}