package com.tienda.inventario.controlador;

import com.tienda.inventario.controlador.dto.AlmacenRequestDTO;
import com.tienda.inventario.controlador.dto.AlmacenResponseDTO;
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
@RequestMapping("/almacenes")
@RequiredArgsConstructor
public class AlmacenWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(Model model) {
        List<AlmacenResponseDTO> almacenes = fachada.obtenerAlmacenes();
        List<StockResponseDTO> alertas = fachada.alertasStockBajo();
        model.addAttribute("almacenes", almacenes);
        model.addAttribute("totalAlertas", alertas.size());
        return "layout/almacenes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("almacen", new AlmacenRequestDTO());
        model.addAttribute("esNuevo", true);
        return "layout/almacenes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("almacen") AlmacenRequestDTO almacen,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "layout/almacenes/form";
        }

        try {
            AlmacenResponseDTO almacenGuardado = fachada.agregarAlmacen(almacen);
            redirectAttributes.addFlashAttribute("mensaje", "Almacén creado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/almacenes";
    }
}
