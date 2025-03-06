package com.gestorinventarios.frontend.components;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FormularioAñadir extends BaseView {

    JButton anadir;
    JButton cancelar;
    JTextField cantidad;
    JTextField precio;
    JLabel mensajeError;
    boolean esVenta;
    TablaCustom tabla;
    JList<String> listaProductos;
    DefaultListModel<String> modeloLista;

    public FormularioAñadir(TablaCustom tabla, String titulo, boolean esVenta, UsuarioController usuarioController,
                            ProductoController productoController,
                            VentaController ventaController) {
        super(titulo, 500, esVenta ? 350 : 400, usuarioController, productoController, ventaController);
        this.esVenta = esVenta;
        this.tabla = tabla;
        crearTitulo(titulo);

        cantidad = (JTextField) crearCampoTexto("Cantidad", false, 2);
        anadir = crearBoton("Añadir");
        cancelar = crearBoton("Cancelar");

        if (!esVenta) {
            JTextField nombreProducto = (JTextField) crearCampoTexto("Nombre", false, 1);
            precio = (JTextField) crearCampoTexto("Precio", false, 3);
            mensajeError = crearMensajeError(4);
            crearPanelBotones(5, anadir, cancelar);
        } else {
            // Crear la lista de productos
            modeloLista = new DefaultListModel<>();
            List<Producto> productos = productoController.obtenerProductos();
            for (Producto producto : productos) {
                modeloLista.addElement(producto.getNombre());
            }

            listaProductos = new JList<>(modeloLista);
            listaProductos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            JScrollPane scrollPane = new JScrollPane(listaProductos);
            scrollPane.setPreferredSize(new java.awt.Dimension(250, 80));

            // Validación si no hay productos disponibles
            if (modeloLista.isEmpty()) {
                mensajeError = crearMensajeError(3);
                mensajeError.setText("No hay productos disponibles para vender.");
                anadir.setEnabled(false);
            } else {
                mensajeError = crearMensajeError(3);
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                mainPanel.add(scrollPane, gbc);
                crearPanelBotones(4, anadir, cancelar);
            }
        }

        anadir.addActionListener(e -> anadir());
        cancelar.addActionListener(e -> dispose());
    }

    private void anadir() {
        String cantidadTexto = cantidad.getText().trim();
        int cantidadValor;

        // Validación de campos vacíos
        if (cantidadTexto.isEmpty()) {
            mensajeError.setText("Todos los campos son obligatorios.");
            return;
        }

        // Validación de número en cantidad
        try {
            cantidadValor = Integer.parseInt(cantidadTexto);
            if (cantidadValor <= 0) {
                mensajeError.setText("La cantidad debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            mensajeError.setText("La cantidad debe ser un número válido.");
            return;
        }

        if (esVenta) {
            List<String> productosSeleccionados = listaProductos.getSelectedValuesList();
            if (productosSeleccionados.isEmpty()) {
                mensajeError.setText("Debe seleccionar al menos un producto.");
                return;
            }

            // Convertir los nombres de productos a objetos `DetalleVenta`
            List<DetalleVenta> detalles = new ArrayList<>();
            for (String nombreProducto : productosSeleccionados) {
                Producto producto = productoController.obtenerPorNombre(nombreProducto);
                if (producto != null) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setProducto(producto);
                    detalle.setCantidad(cantidadValor);
                    detalle.setPrecioUnitario(producto.getPrecio());
                    detalles.add(detalle);
                }
            }

            ventaController.registrarVenta(1, detalles); // Suponiendo que clienteId=1
            ventaController.actualizarVentas(tabla);
            JOptionPane.showMessageDialog(this, "Venta registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
