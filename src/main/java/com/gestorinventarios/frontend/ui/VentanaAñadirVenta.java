package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.gestorinventarios.frontend.utils.Utiles.validarInt;

public class VentanaAñadirVenta extends BaseView {
    private JButton anadir, cancelar, añadirProducto;
    private JLabel mensajeError;
    private TablaCustom tabla;
    private JList<String> listaProductos;
    private DefaultListModel<String> modeloLista;
    private Map<String, JTextField> camposCantidad; // Mapa para almacenar las cantidades de cada producto
    private DefaultTableModel modeloTablaProductos;
    private JTable tablaProductosSeleccionados;

    public VentanaAñadirVenta(TablaCustom tabla) {
        super("Añadir Venta", 600, 500);
        this.tabla = tabla;
        crearTitulo("Añadir Venta");

        anadir = crearBoton("Añadir Venta");
        cancelar = crearBoton("Cancelar");
        añadirProducto = crearBoton("Añadir Producto");

        configurarPanelVenta();

        añadirProducto.addActionListener(e -> agregarProductoALista());
        anadir.addActionListener(e -> anadirVenta());
        cancelar.addActionListener(e -> dispose());
    }

    private void configurarPanelVenta() {
        modeloLista = new DefaultListModel<>();
        camposCantidad = new HashMap<>();
        List<Producto> productos = productoController.obtenerProductos();
        productos.forEach(producto -> modeloLista.addElement(producto.getNombre()));

        listaProductos = new JList<>(modeloLista);
        listaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaProductos);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        mensajeError = crearMensajeError(3);

        JPanel panelCantidad = new JPanel(new GridBagLayout());
        panelCantidad.setBorder(BorderFactory.createTitledBorder("Seleccione la cantidad"));
        listaProductos.addListSelectionListener(e -> actualizarPanelCantidad(panelCantidad));

        modeloTablaProductos = new DefaultTableModel(new String[]{"Producto", "Cantidad"}, 0);
        tablaProductosSeleccionados = new JTable(modeloTablaProductos);
        JScrollPane scrollTablaProductos = new JScrollPane(tablaProductosSeleccionados);
        scrollTablaProductos.setPreferredSize(new Dimension(250, 100));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(scrollPane, gbc);

        gbc.gridy = 2;
        mainPanel.add(panelCantidad, gbc);

        gbc.gridy = 3;
        mainPanel.add(añadirProducto, gbc);

        gbc.gridy = 4;
        mainPanel.add(scrollTablaProductos, gbc);

        gbc.gridy = 5;
        crearPanelBotones(gbc.gridy, anadir, cancelar);
    }

    private void actualizarPanelCantidad(JPanel panelCantidad) {
        panelCantidad.removeAll();
        panelCantidad.setLayout(new GridBagLayout());

        String seleccionado = listaProductos.getSelectedValue();
        if (seleccionado == null) return;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label = new JLabel(seleccionado + ": ");
        JTextField campoCantidad = new JTextField(5);
        campoCantidad.setText("1");

        camposCantidad.put(seleccionado, campoCantidad);

        panelCantidad.add(label, gbc);
        gbc.gridx++;
        panelCantidad.add(campoCantidad, gbc);

        panelCantidad.revalidate();
        panelCantidad.repaint();
    }

    private void agregarProductoALista() {
        String seleccionado = listaProductos.getSelectedValue();
        if (seleccionado == null) {
            mensajeError.setText("Seleccione un producto.");
            return;
        }

        int cantidadValor = validarInt(camposCantidad.get(seleccionado).getText().trim());
        if (cantidadValor <= 0) {
            mensajeError.setText("La cantidad debe ser mayor a 0.");
            return;
        }

        // Verificar si ya existe en la tabla
        for (int i = 0; i < modeloTablaProductos.getRowCount(); i++) {
            if (modeloTablaProductos.getValueAt(i, 0).equals(seleccionado)) {
                int nuevaCantidad = (Integer) modeloTablaProductos.getValueAt(i, 1) + cantidadValor;
                modeloTablaProductos.setValueAt(nuevaCantidad, i, 1);
                return;
            }
        }

        // Agregar nuevo producto si no existía antes
        modeloTablaProductos.addRow(new Object[]{seleccionado, cantidadValor});
    }

    private void anadirVenta() {
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

        if (detalles.isEmpty()) return;

        ventaController.registrarVenta(1, detalles);
        tabla.actualizarTabla();
        JOptionPane.showMessageDialog(this, "Venta registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
