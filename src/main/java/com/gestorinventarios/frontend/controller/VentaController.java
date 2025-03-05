package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.service.ProductoService;
import com.gestorinventarios.backend.service.VentaService;
import com.gestorinventarios.frontend.components.TablaCustom;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class VentaController {
    private final VentaService ventaService;
    private final ProductoService productoService;

    public VentaController(VentaService ventaService, ProductoService productoService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    public List<Venta> obtenerVentas() {
        return ventaService.listarVentas();
    }

    public void registrarVenta(String nombreProducto, int cantidad) {
        Producto producto = productoService.obtenerPorNombre(nombreProducto);
        if (producto == null) {
            throw new IllegalArgumentException("El producto no existe en la base de datos");
        }

        LocalDateTime fecha = LocalDateTime.now();
        Venta venta = new Venta(null, producto, cantidad, fecha);

        ventaService.guardarVenta(venta);
    }

    @Transactional
    public void eliminarVenta(long idVenta) {
        ventaService.eliminarVenta(idVenta);
    }

    public int obtenerNumVentas() {
        return ventaService.obtenerNumVentas();
    }

    public double obtenerTotalIngresos() {
        return ventaService.obtenerTotalIngresos();
    }

    public void actualizarVentas(TablaCustom ventasTable) {
        DefaultTableModel model = ventasTable.getTableModel();
        model.setRowCount(0);
        for (Venta venta : obtenerVentas()) {
            double precio = venta.getProducto().getPrecio() * venta.getCantidadVendida();
            model.addRow(new Object[]{
                    venta.getId(),
                    venta.getProducto().getNombre(),
                    venta.getCantidadVendida(),
                    precio + "€",
                    venta.getFechaVenta()
            });
        }
    }
}