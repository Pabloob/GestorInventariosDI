package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.service.DetallesVentaService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class DetallesVentaController {
    private final DetallesVentaService detallesVentaService;
    private final ProductoController productoController;
    private final VentaController ventaController;

    public DetallesVentaController(DetallesVentaService detallesVentaService, ProductoController productoController, VentaController ventaController) {
        this.detallesVentaService = detallesVentaService;
        this.productoController = productoController;
        this.ventaController = ventaController;
    }

    public List<DetalleVenta> obtenerDetallesPorVenta(Long idVenta) {
        return detallesVentaService.obtenerDetallesPorVenta(idVenta);
    }

    public List<DetalleVenta> obtenerDetallesPorVentaYProducto(Long idVenta, String nombreProducto) {
        return detallesVentaService.obtenerDetallesPorVentaYProducto(idVenta, productoController.obtenerPorNombre(nombreProducto).getId());
    }

    public List<DetalleVenta> obtenerDetallesPorFechaVenta(LocalDate fechaVenta) {
        return detallesVentaService.obtenerDetallesPorFechaVenta(fechaVenta);
    }
    public List<DetalleVenta> obtenerDetallesPorFechaVentaYProducto(LocalDate fechaVenta,String nombreProducto) {
        return detallesVentaService.obtenerDetallesPorFechaVentaYProducto(fechaVenta,nombreProducto);
    }

    public int obtenerCantidadPorIdVentaYProducto(long idVenta, String nombreProducto) {
        return detallesVentaService.obtenerCantidadPorIdVentaYProducto(idVenta,productoController.obtenerPorNombre(nombreProducto).getId());
    }

    public void guardarDetalleVenta(DetalleVenta detalleVenta) {
        detallesVentaService.guardarDetalleVenta(detalleVenta);
    }

    public List<DetalleVenta> obtenerTodosLosDetallesVenta() {
        return detallesVentaService.obtenerTodosLosDetallesVenta();
    }
}
