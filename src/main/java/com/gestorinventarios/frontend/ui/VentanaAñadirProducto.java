package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;

import javax.swing.*;

import static com.gestorinventarios.frontend.utils.Utiles.validarDouble;
import static com.gestorinventarios.frontend.utils.Utiles.validarInt;

public class VentanaAñadirProducto extends BaseView {
    private JButton anadir, cancelar;
    private JTextField cantidad, precio, nombreProducto;
    private JLabel mensajeError;
    private TablaCustom tabla;

    public VentanaAñadirProducto(TablaCustom tabla) {
        super("Añadir producto", 500, 400);
        this.tabla = tabla;
        crearTitulo("Añadir producto");

        cantidad = (JTextField) crearCampoTexto("Cantidad", false, 2);
        anadir = crearBoton("Añadir");
        cancelar = crearBoton("Cancelar");

        configurarPanelProducto();

        anadir.addActionListener(e -> anadirProducto());
        cancelar.addActionListener(e -> dispose());
    }

    private void configurarPanelProducto() {
        nombreProducto = (JTextField) crearCampoTexto("Nombre", false, 1);
        precio = (JTextField) crearCampoTexto("Precio", false, 3);
        mensajeError = crearMensajeError(4);
        crearPanelBotones(5, anadir, cancelar);
    }

    private void anadirProducto() {
        String nombre = nombreProducto.getText().trim();
        if (nombre.isEmpty()) {
            mensajeError.setText("El nombre del producto es obligatorio.");
            return;
        }

        int stock = validarInt(cantidad.getText().trim());
        if (stock == -1) return;

        double precioValor = validarDouble(precio.getText().trim());
        if (precioValor == -1) return;

        productoController.registrarProducto(nombre, precioValor, stock);
        tabla.actualizarTabla();
        JOptionPane.showMessageDialog(this, "Producto registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
