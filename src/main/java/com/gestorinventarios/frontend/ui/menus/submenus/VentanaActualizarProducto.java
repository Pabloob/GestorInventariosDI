package com.gestorinventarios.frontend.ui.menus.submenus;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;

import javax.swing.*;
import java.awt.*;

public class VentanaActualizarProducto extends BaseView {
    private final JTextField nombreProducto, precio, cantidad;
    private final JLabel mensajeError;
    private final Producto producto;
    private final JComboBox<String> estadoProducto;
    private final BotonConEstilo actualizar, cancelar;

    public VentanaActualizarProducto(Producto producto) {
        super("Actualizar Producto", 500, 400);
        this.producto = producto;

        crearTitulo("Actualizar Producto");
        nombreProducto = (JTextField) crearCampoTexto("Nombre", false, 1);
        precio = (JTextField) crearCampoTexto("Precio", false, 2);
        cantidad = (JTextField) crearCampoTexto("Cantidad", false, 3);

        GridBagConstraints gbcCampoTexto = new GridBagConstraints();
        gbcCampoTexto.gridx = 1;
        gbcCampoTexto.gridy = 4;
        gbcCampoTexto.fill = GridBagConstraints.HORIZONTAL;
        gbcCampoTexto.insets = new Insets(5, 0, 5, 5);
        estadoProducto = new JComboBox<>(new String[]{"Activo", "Inactivo"});
        mainPanel.add(estadoProducto, gbcCampoTexto);

        mensajeError = crearMensajeError(5);

        actualizar = new BotonConEstilo("Actualizar", this::actualizarProducto);
        cancelar = new BotonConEstilo("Cancelar", this::dispose);
        crearPanelBotones(6, actualizar, cancelar);

        nombreProducto.setText(producto.getNombre());
        precio.setText(String.valueOf(producto.getPrecio()));
        cantidad.setText(String.valueOf(producto.getCantidad()));
        estadoProducto.setSelectedIndex(producto.getActivo() == 1 ? 0 : 1);
    }

    private void actualizarProducto() {
        String nuevoNombre = nombreProducto.getText().trim();
        double nuevoPrecio;
        int nuevaCantidad;
        int nuevoEstado = estadoProducto.getSelectedIndex() == 0 ? 1 : 0;

        try {
            nuevoPrecio = Double.parseDouble(precio.getText().trim());
            nuevaCantidad = Integer.parseInt(cantidad.getText().trim());
        } catch (NumberFormatException e) {
            mensajeError.setText("Precio y cantidad deben ser valores numéricos.");
            return;
        }

        if (nuevoNombre.isEmpty() || nuevoPrecio <= 0 || nuevaCantidad < 0) {
            mensajeError.setText("Ingrese valores válidos.");
            return;
        }

        productoController.actualizarProducto(producto.getId(), nuevoNombre, nuevoPrecio, nuevaCantidad, nuevoEstado);
        JOptionPane.showMessageDialog(this, "Producto actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}