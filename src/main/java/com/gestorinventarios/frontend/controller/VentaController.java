package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.service.DetallesVentaService;
import com.gestorinventarios.backend.service.ProductoService;
import com.gestorinventarios.backend.service.VentaService;
import com.gestorinventarios.frontend.components.ComboBoxCellEditor;
import com.gestorinventarios.frontend.components.TablaCustom;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class VentaController {
    private final VentaService ventaService;
    private final ProductoService productoService;
    private final DetallesVentaService detallesVentaService;

    public VentaController(VentaService ventaService, ProductoService productoService, DetallesVentaService detallesVentaService) {
        this.ventaService = ventaService;
        this.productoService = productoService;
        this.detallesVentaService = detallesVentaService;
    }

    public List<Venta> obtenerVentas() {
        return ventaService.listarVentas();
    }

    public void registrarVenta(int clienteId, List<DetalleVenta> detalles) {
        LocalDateTime fecha = LocalDateTime.now();
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


    public void actualizarVentas(TablaCustom ventasTable) {
        DefaultTableModel model = ventasTable.getTableModel();
        model.setRowCount(0);

        JTable table = ventasTable.getTable();

        for (Venta venta : ventaService.listarVentas()) {
            List<DetalleVenta> detalles = detallesVentaService.obtenerDetallesPorVenta(venta.getId());

            String[] productos;
            boolean multipleProductos = detalles.size() > 1;

            if (multipleProductos) {
                productos = detalles.stream()
                        .map(detalle -> detalle.getProducto().getNombre())
                        .toArray(String[]::new);
            } else if (!detalles.isEmpty()) {
                productos = new String[]{detalles.get(0).getProducto().getNombre()};
            } else {
                productos = new String[0];
            }
            Object productoCellValue = multipleProductos ? productos : (productos.length > 0 ? productos[0] : "");

            double precioTotal = detalles.stream()
                    .mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario())
                    .sum();

            model.addRow(new Object[]{
                    venta.getId(),
                    productoCellValue,
                    multipleProductos ? "Varios productos" : detalles.get(0).getCantidad(),
                    precioTotal + "€",
                    venta.getFechaVenta()
            });


            // Asignamos el editor SOLO a la celda si hay más de un producto
            if (multipleProductos) {
                table.getColumnModel().getColumn(1).setCellEditor(new ComboBoxCellEditor());
            }
        }
    }

}
