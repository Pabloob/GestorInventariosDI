package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.service.DetallesVentaService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

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

    public List<DetalleVenta> obtenerDetallesVenta(Long idVenta) {
        return detallesVentaService.obtenerDetallesVenta(idVenta);
    }

    public int obtenerCantidadPorIdVentaYProducto(long idVenta, String nombreProducto) {
        Producto producto = productoController.obtenerPorNombre(nombreProducto);
        return (producto != null) ? detallesVentaService.obtenerCantidadPorIdVentaYProducto(idVenta, producto.getId()) : 0;
    }

    public boolean actualizarDetallesVenta(Long id, List<DetalleVenta> detalles) {
        if (productoController.existeStock(detalles)) {
            detallesVentaService.actualizarDetallesVenta(ventaController.obtenerVenta(id), detalles);
            return true;
        }
        return false;

    }
}

