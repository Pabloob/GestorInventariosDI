package com.gestorinventarios.frontend.components.Tabla;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.frontend.controller.DetallesVentaController;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.VentaController;
import com.toedter.calendar.JDateChooser;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.gestorinventarios.frontend.utils.Utiles.validarDouble;
import static com.gestorinventarios.frontend.utils.Utiles.validarInt;

public class TablaCustom extends JScrollPane {
    @Getter
    private JTable table;
    @Getter
    private DefaultTableModel tableModel;

    private final ProductoController productoController;
    private final VentaController ventanaController;
    private final DetallesVentaController detallesVentaController;
    private final String titulo;
    private final String[] columnas;
    private boolean esEditable = true;

    public TablaCustom(String titulo, String[] columnas, ProductoController productoController) {
        this.titulo = titulo;
        this.columnas = columnas;
        this.productoController = productoController;
        this.ventanaController = null;
        this.detallesVentaController = null;
        inicializarTabla();
    }

    public TablaCustom(String titulo, String[] columnas, VentaController ventanaController, DetallesVentaController detallesVentaController) {
        this.titulo = titulo;
        this.columnas = columnas;
        this.ventanaController = ventanaController;
        this.detallesVentaController = detallesVentaController;
        this.productoController = null;
        inicializarTabla();
    }

    private void inicializarTabla() {
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(UIManager.getColor("Label.foreground"));

        tableModel = new DefaultTableModel(columnas, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                TableCellEditor editor = getCellEditor(row, column);
                return editor instanceof ProductoComboBoxEditor;
            }
        };

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Crear un panel contenedor para el filtro y la tabla
        JPanel panelContenido = new JPanel(new BorderLayout());
        panelContenido.add(new JScrollPane(table), BorderLayout.CENTER);

        // Crear el panel de filtro
        if (esEditable) {
            JPanel panelFiltro = crearPanelFiltro(sorter);
            panelContenido.add(panelFiltro, BorderLayout.NORTH);
        }

        // Usar ScrollPaneLayout como layout principal
        JScrollPane scrollPane = new JScrollPane(panelContenido);
        scrollPane.setLayout(new ScrollPaneLayout());

        // Agregar el JScrollPane como el único componente principal
        setViewportView(scrollPane);
        setBorder(BorderFactory.createTitledBorder(titulo));

        //Actualizar tablas
        actualizarTabla();
    }

    private JPanel crearPanelFiltro(TableRowSorter<DefaultTableModel> sorter) {
        JPanel panelFiltro = new JPanel();

        if (productoController != null) {
            JTextField campoNombre = new JTextField(10);
            JTextField campoStock = new JTextField(5);
            JTextField campoPrecio = new JTextField(5);
            JButton botonFiltrar = new JButton("Filtrar");

            botonFiltrar.addActionListener(e -> aplicarFiltroProductos(campoNombre, campoStock, campoPrecio));

            panelFiltro.add(new JLabel("Nombre: "));
            panelFiltro.add(campoNombre);
            panelFiltro.add(new JLabel("Stock: "));
            panelFiltro.add(campoStock);
            panelFiltro.add(new JLabel("Precio: "));
            panelFiltro.add(campoPrecio);
            panelFiltro.add(botonFiltrar);

        } else if (ventanaController != null && detallesVentaController != null) {
            JTextField campoProducto = new JTextField(5);
            JDateChooser campoFecha = new JDateChooser();
            JButton botonFiltrar = new JButton("Filtrar");
            botonFiltrar.addActionListener(e -> aplicarFiltroVentas(campoProducto, campoFecha));

            panelFiltro.add(new JLabel("Producto: "));
            panelFiltro.add(campoProducto);
            panelFiltro.add(new JLabel("Fecha: "));
            panelFiltro.add(campoFecha);
            panelFiltro.add(botonFiltrar);
        }

        return panelFiltro;
    }

    private void aplicarFiltroProductos(JTextField campoNombre, JTextField campoStock, JTextField campoPrecio) {
        List<Object> filtros = new ArrayList<>();

        String filtroNombre = campoNombre.getText().trim();
        if (!filtroNombre.isEmpty()) {
            filtros.add(filtroNombre);
        } else {
            filtros.add(null);
        }

        int filtroStock = validarInt(campoStock.getText().trim());
        if (filtroStock != -1) {
            filtros.add(filtroStock);
        } else {
            filtros.add(null);
        }

        double filtroPrecio = validarDouble(campoPrecio.getText().trim());
        if (filtroPrecio != -1) {
            filtros.add(filtroPrecio);
        } else {
            filtros.add(null);
        }

        actualizarTablaProductos(filtros.toArray());
    }

    private void aplicarFiltroVentas(JTextField campoProducto, JDateChooser campoFecha) {
        LocalDate filtroDate = (campoFecha.getDate() != null)
                ? campoFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                : null;

        String filtroProducto = campoProducto.getText().trim();
        if (filtroProducto.isEmpty()) {
            filtroProducto = null;
        }

        Object[] filtrosVenta = new Object[]{filtroDate, filtroProducto};
        actualizarTablaVentas(filtrosVenta);
    }

    public void actualizarTabla() {
        if (productoController != null) {
            actualizarTablaProductos(null);
        } else {
            actualizarTablaVentas(null);
        }
    }

    private void actualizarTablaProductos(Object[] filtrosProductos) {
        getTableModel().setRowCount(0);
        List<Producto> productos;

        if (filtrosProductos != null) {
            productos = productoController.obtenerProductos(filtrosProductos);
        } else {
            productos = productoController.obtenerProductos();
        }

        for (Producto producto : productos) {
            if (producto.getActivo() == 1) {
                getTableModel().addRow(new Object[]{
                        producto.getNombre(),
                        producto.getCantidad(),
                        producto.getPrecio() + "€",
                });
            }
        }
    }

    private void actualizarTablaVentas(Object[] filtrosVenta) {

        getTableModel().setRowCount(0);

        Map<Integer, String[]> productosPorFila = new HashMap<>();

        List<Venta> ventas;
        if (filtrosVenta != null && filtrosVenta[0] != null) {
            ventas = ventanaController.listarVentasPorFecha((LocalDate) filtrosVenta[0]);
        } else {
            ventas = ventanaController.listarVentas();
        }

        int rowIndex = 0;
        for (Venta venta : ventas) {
            List<DetalleVenta> detalles;
            if (filtrosVenta != null && filtrosVenta[1] != null) {
                detalles = detallesVentaController.obtenerDetallesPorVentaYProducto(venta.getId(), (String) filtrosVenta[1]);
            } else {
                detalles = detallesVentaController.obtenerDetallesPorVenta(venta.getId());
            }

            String[] productos = detalles.stream()
                    .map(detalle -> detalle.getProducto().getNombre())
                    .toArray(String[]::new);

            Map<String, Integer> productoCantidadMap = detalles.stream()
                    .collect(Collectors.toMap(detalle -> detalle.getProducto().getNombre(), DetalleVenta::getCantidad));

            String productoSeleccionado = productos.length > 0 ? productos[0] : "";
            int cantidadInicial = productos.length > 0 ? productoCantidadMap.getOrDefault(productoSeleccionado, 0) : 0;

            productosPorFila.put(rowIndex, productos);

            if (productos.length >= 1) {

                getTableModel().addRow(new Object[]{
                        venta.getId(),
                        productoSeleccionado,
                        cantidadInicial,
                        detalles.stream().mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario()).sum() + "€",
                        venta.getFechaVenta()
                });

                rowIndex++;
            }
        }

        table.getColumnModel().getColumn(1).setCellEditor(new ProductoComboBoxEditor(productosPorFila, detallesVentaController, table));
        table.getColumnModel().getColumn(1).setCellRenderer(new ProductoComboBoxRenderer());
    }

    public void setEsEditable(boolean esEditable) {
        this.esEditable = esEditable;
        inicializarTabla();
    }
}