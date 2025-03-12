package com.gestorinventarios.frontend.ui.menus.submenus;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.gestorinventarios.frontend.utils.Utiles.*;

public class MenuVentanaVentas extends BaseView {
    private final BotonConEstilo anadir, actualizarVenta, cancelar, anadirProducto, eliminarProducto, botonGuardarCantidad;
    private final JLabel mensajeError;

    private final DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private final DefaultTableModel modeloTablaProductos;

    private final JList<String> listaProductos;
    private final JTable tablaProductosSeleccionados;

    private JPanel panelEditarCantidad;
    private final JLabel nuevaCantidad = new JLabel();
    private JTextField campoEditarCantidad;

    private JPanel panelCantidad;
    private final JLabel cantidad = new JLabel();
    private JTextField campoCantidad;

    private final Venta venta;
    private final List<DetalleVenta> detalleVentas;

    public MenuVentanaVentas(Venta venta, List<DetalleVenta> detalleVentas) {
        super("Actualizar Venta", 700, 900);
        this.venta = venta;
        this.detalleVentas = detalleVentas;
        crearTitulo("Actualizar Venta");
        anadir = null;
        actualizarVenta = new BotonConEstilo("Actualizar Venta", this::actualizarVenta);
        cancelar = new BotonConEstilo("Cancelar", this::dispose);
        anadirProducto = new BotonConEstilo("Añadir Producto", this::agregarProductoALista);
        eliminarProducto = new BotonConEstilo("Eliminar Producto", this::eliminarProductoDeLista);
        botonGuardarCantidad = new BotonConEstilo("Guardar", this::guardarNuevaCantidad);

        modeloTablaProductos = new DefaultTableModel(new String[]{"Producto", "Cantidad"}, 0);
        listaProductos = new JList<>(modeloLista);
        tablaProductosSeleccionados = new JTable(modeloTablaProductos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductosSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mensajeError = crearMensajeError(2);

        configurarPaneles();
        cargarProductos();
        cargarDetallesVenta();
    }

    public MenuVentanaVentas() {
        super("Añadir Venta", 700, 900);
        this.venta = null;
        this.detalleVentas = null;
        crearTitulo("Añadir Venta");
        actualizarVenta = null;
        anadir = new BotonConEstilo("Añadir Venta", this::anadirVenta);
        cancelar = new BotonConEstilo("Cancelar", this::dispose);
        anadirProducto = new BotonConEstilo("Añadir Producto", this::agregarProductoALista);
        eliminarProducto = new BotonConEstilo("Eliminar Producto", this::eliminarProductoDeLista);
        botonGuardarCantidad = new BotonConEstilo("Guardar", this::guardarNuevaCantidad);

        listaProductos = new JList<>(modeloLista);
        listaProductos.setFocusable(false);

        modeloTablaProductos = new DefaultTableModel(new String[]{"Producto", "Cantidad"}, 0);
        tablaProductosSeleccionados = new JTable(modeloTablaProductos) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductosSeleccionados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mensajeError = crearMensajeError(2);

        configurarPaneles();
        cargarProductos();
    }

    private void configurarPaneles() {
        JScrollPane scrollListaProductos = new JScrollPane(listaProductos);
        JScrollPane scrollTablaProductos = new JScrollPane(tablaProductosSeleccionados);

        scrollListaProductos.setPreferredSize(new Dimension(300, 250));
        scrollTablaProductos.setPreferredSize(new Dimension(300, 250));

        crearPanelComponentes(1, 0, scrollListaProductos, scrollTablaProductos);

        panelCantidad = crearPanelCantidad();
        panelEditarCantidad = crearPanelEditarCantidad();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(panelCantidad, gbc);
        mainPanel.add(panelEditarCantidad, gbc);

        gbc.gridy = 4;
        crearPanelBotones(gbc.gridy, anadirProducto, eliminarProducto);
        gbc.gridy = 5;
        if (anadir != null) {
            crearPanelBotones(gbc.gridy, anadir, cancelar);
        } else {
            crearPanelBotones(gbc.gridy, actualizarVenta, cancelar);
        }

        listaProductos.addListSelectionListener(e -> alternarPanelesCantidad(true));
        tablaProductosSeleccionados.getSelectionModel().addListSelectionListener(e -> alternarPanelesCantidad(false));
    }

    private JPanel crearPanelCantidad() {
        return crearPanelCantidadBase("Seleccione la cantidad", cantidad, campoCantidad = new JTextField(5), anadirProducto);
    }

    private JPanel crearPanelEditarCantidad() {
        return crearPanelCantidadBase("Editar Cantidad", nuevaCantidad, campoEditarCantidad = new JTextField(5), botonGuardarCantidad);
    }

    private JPanel crearPanelCantidadBase(String titulo, JLabel nombreProducto, JTextField campo, BotonConEstilo boton) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.setVisible(false);
        panel.setPreferredSize(new Dimension(400, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(nombreProducto, gbc);

        gbc.gridx++;
        panel.add(campo, gbc);

        gbc.gridx++;
        panel.add(boton, gbc);

        return panel;
    }

    private void cargarProductos() {
        List<Producto> productos = productoController.obtenerProductos();
        productos.forEach(producto -> modeloLista.addElement(producto.getNombre()));
    }

    private void alternarPanelesCantidad(boolean mostrarCantidad) {
        campoEditarCantidad.setText(null);
        campoCantidad.setText(null);
        mensajeError.setText(null);

        if (mostrarCantidad) {
            panelCantidad.setVisible(true);
            cantidad.setText(listaProductos.getSelectedValue());
            panelEditarCantidad.setVisible(false);
            tablaProductosSeleccionados.clearSelection();
        } else {
            int row = tablaProductosSeleccionados.getSelectedRow();
            if (row == -1) return;

            panelEditarCantidad.setVisible(true);
            nuevaCantidad.setText((String) modeloTablaProductos.getValueAt(row, 0));
            campoEditarCantidad.setText(String.valueOf(modeloTablaProductos.getValueAt(row, 1)));
            panelCantidad.setVisible(false);
            listaProductos.clearSelection();
        }
    }

    private void guardarNuevaCantidad() {
        mensajeError.setText(null);

        int row = tablaProductosSeleccionados.getSelectedRow();
        if (row == -1) {
            mensajeError.setText("Seleccione un producto para actualizar la cantidad.");
            return;
        }
        try {
            int nuevaCantidad = Integer.parseInt(campoEditarCantidad.getText().trim());
            if (nuevaCantidad <= 0) {
                mensajeError.setText("La cantidad debe ser mayor a 0.");
                return;
            }
            modeloTablaProductos.setValueAt(nuevaCantidad, row, 1);
        } catch (NumberFormatException e) {
            mensajeError.setText("Ingrese un número válido.");
        }
    }

    private void anadirVenta() {
        mensajeError.setText(null);

        if (modeloTablaProductos.getRowCount() == 0) {
            mensajeError.setText("Debe agregar al menos un producto.");
            return;
        }

        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
            String nombreProducto = (String) modeloTablaProductos.getValueAt(i, 0);
            int cantidad = (Integer) modeloTablaProductos.getValueAt(i, 1);

            Optional.ofNullable(productoController.obtenerPorNombre(nombreProducto))
                    .ifPresent(producto -> {
                        DetalleVenta detalle = new DetalleVenta();
                        detalle.setProducto(producto);
                        detalle.setCantidad(cantidad);
                        detalle.setPrecioUnitario(producto.getPrecio());
                        detalles.add(detalle);
                    });
        }

        if (!detalles.isEmpty() && ventaController.anadirVenta(1, detalles)) {
            JOptionPane.showMessageDialog(this, "Venta registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No hay stock suficiente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarProductoALista() {
        mensajeError.setText(null);

        String seleccionado = listaProductos.getSelectedValue();
        if (seleccionado == null) {
            mensajeError.setText("Seleccione un producto.");
            return;
        }

        int cantidadValor = validarIntMayor0(campoCantidad.getText());
        if (cantidadValor <= 0) {
            mensajeError.setText("La cantidad debe ser mayor a 0.");
            return;
        }

        if (!productoController.existeStock(seleccionado, cantidadValor)) {
            mensajeError.setText("No hay stock suficiente");
            return;
        }
        for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
            if (modeloTablaProductos.getValueAt(i, 0).equals(seleccionado)) {
                modeloTablaProductos.setValueAt((int) modeloTablaProductos.getValueAt(i, 1) + cantidadValor, i, 1);
                return;
            }
        }
        modeloTablaProductos.addRow(new Object[]{seleccionado, cantidadValor});
    }

    private void eliminarProductoDeLista() {
        mensajeError.setText(null);

        int row = tablaProductosSeleccionados.getSelectedRow();
        if (row == -1) {
            mensajeError.setText("Seleccione un producto para eliminar.");
            return;
        }
        modeloTablaProductos.removeRow(row);
    }

    private void actualizarVenta() {
        mensajeError.setText(null);

        if (modeloTablaProductos.getRowCount() == 0) {
            mensajeError.setText("Debe agregar al menos un producto.");
            return;
        }

        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
            String nombreProducto = (String) modeloTablaProductos.getValueAt(i, 0);
            int cantidad = (Integer) modeloTablaProductos.getValueAt(i, 1);
            Producto producto = productoController.obtenerPorNombre(nombreProducto);
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detalles.add(detalle);
        }

        if (!detalles.isEmpty() && detallesVentaController.actualizarDetallesVenta(venta.getId(), detalles)) {
            mostrarMensajeInformacion(this, "Venta actualizada con éxito", "Éxito");
            dispose();
        } else {
            mostrarMensajeError(this, "No hay stock suficiente", "Error");
        }
    }

    private void cargarDetallesVenta() {
        for (DetalleVenta detalle : detalleVentas) {
            modeloTablaProductos.addRow(new Object[]{detalle.getProducto().getNombre(), detalle.getCantidad()});
        }
    }

}