package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.service.DetallesVentaService;
import com.gestorinventarios.backend.service.ProductoService;
import com.gestorinventarios.backend.service.VentaService;
import com.gestorinventarios.frontend.components.Tabla.ProductoComboBoxEditor;
import com.gestorinventarios.frontend.components.Tabla.ProductoComboBoxRenderer;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class VentaController {
    private final VentaService ventaService;
    private final DetallesVentaService detallesVentaService;

    public VentaController(VentaService ventaService, DetallesVentaService detallesVentaService) {
        this.ventaService = ventaService;
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

        model.addColumn("ProductosOcultos");

        for (int rowIndex = 0; rowIndex < ventaService.listarVentas().size(); rowIndex++) {
            Venta venta = ventaService.listarVentas().get(rowIndex);
            List<DetalleVenta> detalles = detallesVentaService.obtenerDetallesPorVenta(venta.getId());

            String[] productos = detalles.stream()
                    .map(detalle -> detalle.getProducto().getNombre())
                    .toArray(String[]::new);

            Map<String, Integer> productoCantidadMap = detalles.stream()
                    .collect(Collectors.toMap(detalle -> detalle.getProducto().getNombre(), DetalleVenta::getCantidad));

            String productoSeleccionado = productos.length > 0 ? productos[0] : "";
            int cantidadInicial = productos.length > 0 ? productoCantidadMap.getOrDefault(productoSeleccionado, 0) : 0;

            model.addRow(new Object[]{
                    venta.getId(),
                    productoSeleccionado,
                    cantidadInicial,
                    detalles.stream().mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario()).sum() + "€",
                    venta.getFechaVenta(),
                    productos
            });
        }

        table.getColumnModel().getColumn(1).setCellEditor(new ProductoComboBoxEditor());
        table.getColumnModel().getColumn(1).setCellRenderer(new ProductoComboBoxRenderer());

        table.removeColumn(table.getColumnModel().getColumn(5));
    }

}
