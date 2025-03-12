package com.gestorinventarios.frontend.components;

import javax.swing.JButton;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonConEstilo extends JButton {

    private Color colorOriginal;

    public BotonConEstilo() {
        super("Botón");
        asignarEstiloBoton();
        agregarEfectoHover();
    }

    public BotonConEstilo(String texto) {
        super(texto);
        asignarEstiloBoton();
        agregarEfectoHover();
    }

    public BotonConEstilo(String texto, Runnable accion) {
        super(texto);
        this.addActionListener(e -> accion.run());
        asignarEstiloBoton();
        agregarEfectoHover();
    }

    private void asignarEstiloBoton() {
        this.setForeground(Color.WHITE);
        this.setFont(new Font("Segoe UI", Font.BOLD, 16));
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        String textoMinus = getText().toLowerCase();

        if (textoMinus.contains("cancelar") || textoMinus.contains("borrar")
                || textoMinus.contains("eliminar") || textoMinus.contains("cerrar")) {
            setBackground(new Color(0xC62828));
        } else if (textoMinus.contains("iniciar sesión")) {
            setBackground(new Color(0x2E7D32));
        } else if (textoMinus.contains("guardar") || textoMinus.contains("registrar") || textoMinus.contains("añadir")) {
            setBackground(new Color(0x0277BD)); 
        } else if (textoMinus.contains("actualizar") || textoMinus.contains("exportar")) {
            setBackground(new Color(0xF57C00)); 
        } else if (textoMinus.contains("filtrar") || textoMinus.contains("limpiar")) {
            setBackground(new Color(0x37474F));
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
        } else if (textoMinus.equals("<") || textoMinus.equals(">")) {
            setBackground(new Color(0x757575)); 
            setPreferredSize(new Dimension(30, 20));
        } else {
            setBackground(new Color(0x0277BD));
        }

        colorOriginal = getBackground(); 
    }

    private void agregarEfectoHover() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(colorOriginal.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(colorOriginal);
            }
        });
    }
}
