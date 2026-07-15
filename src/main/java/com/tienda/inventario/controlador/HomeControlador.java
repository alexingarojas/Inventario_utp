package com.tienda.inventario.controlador;

import com.tienda.inventario.controlador.dto.AlmacenResponseDTO;
import com.tienda.inventario.controlador.dto.ProductoResponseDTO;
import com.tienda.inventario.controlador.dto.StockResponseDTO;
import com.tienda.inventario.patron.fachada.InventarioFachada;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeControlador {

    private final InventarioFachada fachada;

    public HomeControlador(InventarioFachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ProductoResponseDTO> productos = fachada.obtenerProductos();
        List<AlmacenResponseDTO> almacenes = fachada.obtenerAlmacenes();
        List<StockResponseDTO> alertas = fachada.alertasStockBajo();

        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("totalAlmacenes", almacenes.size());
        model.addAttribute("totalAlertas", alertas.size());

        return "layout/home";
    }
}