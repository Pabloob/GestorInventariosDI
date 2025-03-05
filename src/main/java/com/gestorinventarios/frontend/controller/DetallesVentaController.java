package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.service.DetallesVentaService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DetallesVentaController {
    private final DetallesVentaService detallesVentaService;

    public DetallesVentaController(DetallesVentaService detallesVentaService) {
        this.detallesVentaService = detallesVentaService;
    }

    public List<DetalleVenta> obtenerDetallesPorVenta(Long idVenta) {
        return detallesVentaService.obtenerDetallesPorVenta(idVenta);
    }

    public void guardarDetalleVenta(DetalleVenta detalleVenta) {
        detallesVentaService.guardarDetalleVenta(detalleVenta);
    }
}
