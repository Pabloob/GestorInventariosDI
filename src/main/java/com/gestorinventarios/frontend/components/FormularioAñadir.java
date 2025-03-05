package com.gestorinventarios.frontend.components;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;

import javax.swing.*;

public class FormularioAñadir extends BaseView {

    JButton anadir;
    JButton cancelar;
    JTextField nombreProducto;
    JTextField cantidad;
    JTextField precio;
    JLabel mensajeError;
    boolean esVenta;
    TablaCustom tabla;
    JComboBox<String> productos;

    public FormularioAñadir(TablaCustom tabla, String titulo, boolean esVenta, UsuarioController usuarioController,
                            ProductoController productoController,
                            VentaController ventaController) {
        super(titulo, 450, esVenta ? 300 : 400, usuarioController, productoController, ventaController);
        this.esVenta = esVenta;
        this.tabla = tabla;
        crearTitulo(titulo);

        cantidad = (JTextField) crearCampoTexto("Cantidad", false, 2);
        anadir = crearBoton("Añadir");
        cancelar = crearBoton("Cancelar");

        if (!esVenta) {
            nombreProducto = (JTextField) crearCampoTexto("Nombre", false, 1);
            precio = (JTextField) crearCampoTexto("Precio", false, 3);
            mensajeError = crearMensajeError(4);
            crearPanelBotones(5, anadir, cancelar);
        } else {
            productos = new JComboBox<>();
            for (Producto producto : productoController.obtenerProductos()) {
                productos.addItem(producto.getNombre());
            }

            if (productos.getItemCount() == 0) {
                mensajeError = crearMensajeError(3);
                mensajeError.setText("No hay productos disponibles para vender.");
                anadir.setEnabled(false);
            } else {
                productos = (JComboBox<String>) crearDesplegable("Productos", productos, 1);
                mensajeError = crearMensajeError(3);
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
        if (cantidadTexto.isEmpty() || (!esVenta && (nombreProducto.getText().trim().isEmpty() || precio.getText().trim().isEmpty()))) {
            mensajeError.setText("Todos los campos son obligatorios.");
            return;
        }

        // Validación de número en cantidad y precio
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
            String nombreSeleccionado = (String) productos.getSelectedItem();
            if (nombreSeleccionado == null) {
                mensajeError.setText("Debe seleccionar un producto.");
                return;
            }

            ventaController.registrarVenta(nombreSeleccionado, cantidadValor);
            ventaController.actualizarVentas(tabla);
            JOptionPane.showMessageDialog(this, "Venta registrada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            double precioValor;
            try {
                precioValor = Double.parseDouble(precio.getText().trim());
                if (precioValor <= 0) {
                    mensajeError.setText("El precio debe ser mayor a 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                mensajeError.setText("El precio debe ser un número válido.");
                return;
            }

            String nombre = nombreProducto.getText().trim();
            if (productoController.obtenerPorNombre(nombre) == null) {
                productoController.registrarProducto(nombre, precioValor, cantidadValor);
                productoController.actualizarProductos(tabla);
                JOptionPane.showMessageDialog(this, "Producto registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                mensajeError.setText("El producto ya existe.");
            }
        }
    }
}
