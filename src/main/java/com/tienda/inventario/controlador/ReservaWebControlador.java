package com.tienda.inventario.controlador;

import com.tienda.inventario.controlador.dto.ReservaRequestDTO;
import com.tienda.inventario.controlador.dto.ProductoResponseDTO;
import com.tienda.inventario.controlador.dto.StockResponseDTO;
import com.tienda.inventario.patron.fachada.InventarioFachada;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", fachada.obtenerProductos());
        model.addAttribute("almacenes", fachada.obtenerAlmacenes());
        List<StockResponseDTO> alertas = fachada.alertasStockBajo();
        model.addAttribute("totalAlertas", alertas.size());
        return "layout/reservas/lista";
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
            return "layout/reservas/ver";
        } catch (Exception e) {
            return "redirect:/reservas";
        }
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) Long almacenId,
            Model model) {
        model.addAttribute("reserva", new ReservaRequestDTO());
        model.addAttribute("productos", fachada.obtenerProductos());
        model.addAttribute("almacenes", fachada.obtenerAlmacenes());
        
        if (productoId != null) {
            model.addAttribute("productoSeleccionado", productoId);
        }
        if (almacenId != null) {
            model.addAttribute("almacenSeleccionado", almacenId);
        }
        
        return "layout/reservas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("reserva") ReservaRequestDTO reserva,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productos", fachada.obtenerProductos());
            model.addAttribute("almacenes", fachada.obtenerAlmacenes());
            return "layout/reservas/form";
        }

        try {
            String resultado = fachada.reservar(reserva);
            redirectAttributes.addFlashAttribute("mensaje", resultado);
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/reservas";
    }

    @PostMapping("/cancelar/{referencia}")
    public String cancelar(@PathVariable String referencia,
                           RedirectAttributes redirectAttributes) {
        try {
            String resultado = fachada.cancelarReserva(referencia);
            redirectAttributes.addFlashAttribute("mensaje", resultado);
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/reservas";
    }
}
