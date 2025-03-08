package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.service.DetallesVentaService;
import com.gestorinventarios.backend.service.VentaService;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Controller
@Getter
public class VentaController {
    private final VentaService ventaService;
    private final DetallesVentaService detallesVentaService;

    public VentaController(VentaService ventaService, DetallesVentaService detallesVentaService) {
        this.ventaService = ventaService;
        this.detallesVentaService = detallesVentaService;
    }

    public List<Venta> listarVentas() {
        return ventaService.listarVentas();
    }

    public List<Venta> listarVentasPorFecha(LocalDate fecha) {
        return ventaService.listarVentasPorFecha(fecha);
    }

    public void registrarVenta(int clienteId, List<DetalleVenta> detalles) {
        LocalDate fecha = LocalDate.now();
        Venta venta = new Venta(null, clienteId, fecha, null);

        Venta nuevaVenta = ventaService.guardarVenta(venta, detalles);

        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(nuevaVenta);
            detallesVentaService.guardarDetalleVenta(detalle);
        }
    }

    @Transactional
    public void eliminarVenta(long idVenta) {
        ventaService.eliminarVenta(idVenta);
    }

    public int obtenerNumVentas() {
        return ventaService.obtenerNumVentas();
    }

    public double obtenerTotalIngresos() {
        return Math.round(ventaService.obtenerTotalIngresos() * 100.0) / 100.0;
    }

}
