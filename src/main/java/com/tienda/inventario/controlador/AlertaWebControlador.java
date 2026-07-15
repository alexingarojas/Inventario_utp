package com.tienda.inventario.controlador;

import com.tienda.inventario.controlador.dto.StockResponseDTO;
import com.tienda.inventario.patron.fachada.InventarioFachada;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/alertas")
@RequiredArgsConstructor
public class AlertaWebControlador {

    private final InventarioFachada fachada;

    @GetMapping
    public String listar(Model model) {
        List<StockResponseDTO> alertas = fachada.alertasStockBajo();
        model.addAttribute("alertas", alertas);
        model.addAttribute("totalAlertas", alertas.size());
        return "layout/alertas/lista";
    }
}
