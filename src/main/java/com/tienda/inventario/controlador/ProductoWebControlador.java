package com.tienda.inventario.controlador;

import com.tienda.inventario.dto.ProductoRequestDTO;
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
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(Model model) {
        List<ProductoResponseDTO> productos = fachada.obtenerProductos();
        List<StockResponseDTO> alertas = fachada.alertasStockBajo();
        model.addAttribute("productos", productos);
        model.addAttribute("totalAlertas", alertas.size());
        return "layout/productos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new ProductoRequestDTO());
        model.addAttribute("esNuevo", true);
        return "layout/productos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") ProductoRequestDTO producto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "layout/productos/form";
        }

        try {
            ProductoResponseDTO productoGuardado = fachada.agregarProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/productos";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductoResponseDTO producto = fachada.obtenerProductoPorId(id);

            // Mapeamos el Response a un Request para poder usar th:field en el form
            ProductoRequestDTO productoRequest = new ProductoRequestDTO();
            productoRequest.setCodigo(producto.getCodigo());
            productoRequest.setNombre(producto.getNombre());
            productoRequest.setDescripcion(producto.getDescripcion());
            productoRequest.setPrecio(producto.getPrecio());
            productoRequest.setStockMinimo(producto.getStockMinimo());

            model.addAttribute("producto", productoRequest);
            model.addAttribute("productoId", id);
            model.addAttribute("esNuevo", false);
            return "layout/productos/form";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Producto no encontrado");
            redirectAttributes.addFlashAttribute("tipo", "danger");
            return "redirect:/productos";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("producto") ProductoRequestDTO producto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productoId", id);
            model.addAttribute("esNuevo", false);
            return "layout/productos/form";
        }

        try {
            fachada.actualizarProducto(id, producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipo", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipo", "danger");
        }

        return "redirect:/productos";
    }


}
