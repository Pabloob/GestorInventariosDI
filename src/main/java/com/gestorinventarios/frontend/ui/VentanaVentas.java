package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.TablaCustom;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.UsuarioController;
import com.gestorinventarios.frontend.controller.VentaController;

import javax.swing.*;

public class VentanaVentas extends BaseView {

    TablaCustom tablaVentas;
    JButton botonBorrar;
    JButton botonActualizar;
    JButton botonAñadir;

    public VentanaVentas(VentanaPrincipal ventanaPrincipal, UsuarioController usuarioController,
                         ProductoController productoController,
                         VentaController ventaController) {
        super("Ventana ventas", 1400, 700,
                usuarioController, productoController, ventaController);

        tablaVentas = ventanaPrincipal.getTablaVentas();
        botonAñadir = crearBoton("Añadir");
        botonActualizar = crearBoton("Actualizar");
        botonBorrar = crearBoton("Borrar");

        crearTitulo("Ventana ventas");
        crearPanelTablas(1, 0, tablaVentas);


        crearPanelBotones(2, botonAñadir, botonActualizar, botonBorrar);

        botonAñadir.addActionListener(e -> abrirFormularioAnadir(tablaVentas,"Añadir venta",true));
        botonBorrar.addActionListener(e -> borrar());

    }

    private void borrar() {
        int row = tablaVentas.getTable().getSelectedRow();
        long idVenta = (long) tablaVentas.getTable().getValueAt(row, 0);
        ventaController.eliminarVenta(idVenta);
        ventaController.actualizarVentas(tablaVentas);
    }
}