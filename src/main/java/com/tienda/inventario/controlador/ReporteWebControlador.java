package com.tienda.inventario.controlador;

import com.tienda.inventario.dto.MovimientoResponseDTO;
import com.tienda.inventario.dto.StockResponseDTO;
import com.tienda.inventario.modelo.Movimiento;
import com.tienda.inventario.fachada.InventarioFachada;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String tipoMovimiento,
            Model model) {

        // Si no hay fechas, usar últimos 30 días
        LocalDate fechaHasta = hasta != null ? hasta : LocalDate.now();
        LocalDate fechaDesde = desde != null ? desde : fechaHasta.minusDays(30);

        // Convertir a LocalDateTime para el servicio (inicio y fin del día)
        LocalDateTime desdeDateTime = fechaDesde.atStartOfDay();
        LocalDateTime hastaDateTime = fechaHasta.atTime(23, 59, 59);

        List<MovimientoResponseDTO> movimientos = fachada.reporte(desdeDateTime, hastaDateTime);
        List<StockResponseDTO> alertas = fachada.alertasStockBajo();

        // Filtro adicional por producto
        if (productoId != null) {
            movimientos = movimientos.stream()
                    .filter(m -> productoId.equals(m.getProductoId()))
                    .collect(Collectors.toList());
        }

        // Filtro adicional por tipo de movimiento
        if (tipoMovimiento != null && !tipoMovimiento.isBlank()) {
            movimientos = movimientos.stream()
                    .filter(m -> tipoMovimiento.equals(m.getTipoMovimiento().name()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("movimientos", movimientos);
        model.addAttribute("totalMovimientos", movimientos.size());
        model.addAttribute("desde", fechaDesde);
        model.addAttribute("hasta", fechaHasta);
        model.addAttribute("productoId", productoId);
        model.addAttribute("tipoMovimiento", tipoMovimiento);
        model.addAttribute("productos", fachada.obtenerProductos());
        model.addAttribute("tiposMovimiento", Movimiento.TipoMovimiento.values());
        model.addAttribute("totalAlertas", alertas.size());
        return "layout/reportes/index";
    }
}
