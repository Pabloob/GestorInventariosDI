package com.gestorinventarios.frontend.ui.menus.submenus;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;

import javax.swing.*;

import static com.gestorinventarios.frontend.utils.Utiles.validarDoubleMayor0;
import static com.gestorinventarios.frontend.utils.Utiles.validarIntMayor0;

public class VentanaAnadirProducto extends BaseView {
    private final BotonConEstilo anadir, cancelar;
    private JTextField cantidad, precio, nombreProducto;
    private JLabel mensajeError;

    public VentanaAnadirProducto() {
        super("Añadir producto", 500, 400);
        crearTitulo("Añadir producto");

        cantidad = (JTextField) crearCampoTexto("Cantidad", false, 2);
        anadir = new BotonConEstilo("Añadir", this::anadirProducto);
        cancelar = new BotonConEstilo("Cancelar", this::dispose);

        configurarPanelProducto();
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

        if (productoController.obtenerPorNombre(nombre) != null) {
            mensajeError.setText("El producto ya existe.");
            return;
        }

        int stock = validarIntMayor0(cantidad.getText().trim());
        if (stock <= 0) {
            mensajeError.setText("La cantidad debe ser mayor a 0.");
            return;
        }

        double precioValor = validarDoubleMayor0(precio.getText().trim());
        if (precioValor <= 0) {
            mensajeError.setText("El precio debe ser mayor a 0.");
            return;
        }

        productoController.anadirProducto(nombre, precioValor, stock);
        JOptionPane.showMessageDialog(this, "Producto registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
