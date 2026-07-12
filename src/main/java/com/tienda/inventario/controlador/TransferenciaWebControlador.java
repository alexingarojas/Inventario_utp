package com.tienda.inventario.controlador;

import com.tienda.inventario.dto.TransferenciaRequestDTO;
import com.tienda.inventario.dto.ProductoResponseDTO;
import com.tienda.inventario.dto.StockResponseDTO;
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
@RequestMapping("/transferencias")
@RequiredArgsConstructor
public class TransferenciaWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", fachada.obtenerProductos());
        model.addAttribute("almacenes", fachada.obtenerAlmacenes());
        return "layout/transferencias/lista";
    }

    @GetMapping("/ver/{productoId}")
    public String verStockProducto(@PathVariable Long productoId, Model model) {
        try {
            ProductoResponseDTO producto = fachada.obtenerProductoPorId(productoId);
            List<StockResponseDTO> stocks = fachada.stockPorProducto(productoId);
            
            model.addAttribute("productoId", productoId);
            model.addAttribute("productoNombre", producto.getNombre());
            model.addAttribute("productoCodigo", producto.getCodigo());
            model.addAttribute("stocks", stocks);
            return "layout/transferencias/ver";
        } catch (Exception e) {
            return "redirect:/transferencias";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long origenId,
            Model model) {
        model.addAttribute("transferencia", new TransferenciaRequestDTO());
        model.addAttribute("productos", fachada.obtenerProductos());
        model.addAttribute("almacenes", fachada.obtenerAlmacenes());
        
        if (productoId != null) {
            model.addAttribute("productoSeleccionado", productoId);
        }
        if (origenId != null) {
            model.addAttribute("origenSeleccionado", origenId);
        }
        
        return "layout/transferencias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("transferencia") TransferenciaRequestDTO transferencia,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productos", fachada.obtenerProductos());
            model.addAttribute("almacenes", fachada.obtenerAlmacenes());
            return "layout/transferencias/form";
        }

        try {
            String resultado = fachada.transferir(transferencia);
            redirectAttributes.addFlashAttribute("mensaje", resultado);
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/transferencias";
    }
}
