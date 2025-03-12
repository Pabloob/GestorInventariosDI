package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.service.VentaService;
import lombok.Getter;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
@Getter
public class VentaController {
    private final VentaService ventaService;
    private final ProductoController productoController;


    public VentaController(VentaService ventaService, ProductoController productoController) {
        this.ventaService = ventaService;
        this.productoController = productoController;
    }

    public int obtenerNumVentas() {
        return ventaService.obtenerNumVentas();
    }

    public int obtenerNumeroVentasFiltradas(Object[] filtrosAplicados) {

        if (filtrosAplicados == null) {
            return obtenerNumVentas();
        }

        return ventaService.obtenerNumeroVentasConDetalles((LocalDate) filtrosAplicados[0], (String) filtrosAplicados[1]);
    }

    public List<Venta> obtenerVentas(int limitePagina, int pagina) {
        return ventaService.obtenerVentas(limitePagina, pagina);
    }

    public List<Venta> obtenerVentas(Object[] filtro, int limitePagina, int pagina) {

        if (filtro == null) {
            return obtenerVentas(limitePagina, pagina);
        } else {
            return ventaService.obtenerVentas((LocalDate) filtro[0], (String) filtro[1], limitePagina, pagina);
        }
    }

    public Venta obtenerVenta(Long idVenta) {
        return ventaService.obtenerVenta(idVenta);
    }

    public boolean anadirVenta(int clienteId, List<DetalleVenta> detalles) {

        if (productoController.existeStock(detalles)) {
            LocalDate fecha = LocalDate.now();
            Venta venta = new Venta(null, clienteId, fecha, null);
            ventaService.anadirVenta(venta, detalles);
            return true;
        }
        return false;

    }

    public double obtenerTotalIngresos() {
        return Math.round(ventaService.obtenerTotalIngresos() * 100.0) / 100.0;
    }

    public boolean eliminarVenta(long idVenta) {
        return ventaService.eliminarVenta(idVenta);
    }

}

