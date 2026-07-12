package com.tienda.inventario.controlador;

import com.tienda.inventario.dto.StockRequestDTO;
import com.tienda.inventario.dto.StockResponseDTO;
import com.tienda.inventario.dto.ProductoResponseDTO;
import com.tienda.inventario.dto.AlmacenResponseDTO;
import com.tienda.inventario.fachada.InventarioFachada;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(Model model) {
        List<ProductoResponseDTO> productos = fachada.obtenerProductos();
        model.addAttribute("productos", productos);
        return "layout/stock/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long almacenId,
            Model model) {
        model.addAttribute("stock", new StockRequestDTO());
        model.addAttribute("productos", fachada.obtenerProductos());
        model.addAttribute("almacenes", fachada.obtenerAlmacenes());
        
        if (productoId != null) {
            model.addAttribute("productoSeleccionado", productoId);
        }
        if (almacenId != null) {
            model.addAttribute("almacenSeleccionado", almacenId);
        }
        
        return "layout/stock/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("stock") StockRequestDTO stock,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productos", fachada.obtenerProductos());
            model.addAttribute("almacenes", fachada.obtenerAlmacenes());
            return "layout/stock/form";
        }

        try {
            StockResponseDTO stockGuardado = fachada.actualizarStock(stock);
            redirectAttributes.addFlashAttribute("mensaje", "Stock actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/stock";
    }

    @GetMapping("/producto/{productoId}")
    public String verStockProducto(@PathVariable Long productoId, Model model) {
        try {
            ProductoResponseDTO producto = fachada.obtenerProductoPorId(productoId);
            List<StockResponseDTO> stocks = fachada.stockPorProducto(productoId);
            model.addAttribute("stocks", stocks);
            model.addAttribute("productoId", productoId);
            model.addAttribute("productoNombre", producto.getNombre());
            model.addAttribute("productoCodigo", producto.getCodigo());
            return "layout/stock/detalle";
        } catch (Exception e) {
            return "redirect:/stock";
        }
    }

    @GetMapping("/almacen/{almacenId}")
    public String verStockAlmacen(@PathVariable Long almacenId, Model model) {
        List<StockResponseDTO> stocks = fachada.stockPorAlmacen(almacenId);
        model.addAttribute("stocks", stocks);
        model.addAttribute("almacenId", almacenId);
        return "layout/stock/detalle";
    }
}
