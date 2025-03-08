package com.gestorinventarios.frontend.components.Tabla;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verificar si la celda tiene un JComboBox
        if (table.getColumnModel().getColumn(column).getCellEditor() instanceof ProductoComboBoxEditor) {
            // Aplicar estilo para celdas con JComboBox
            component.setBackground(new Color(100, 149, 237)); // Color de fondo
            component.setForeground(Color.WHITE); // Color de texto
            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER); // Alinear al centro
        } else {
            // Aplicar estilo para celdas sin JComboBox
            component.setBackground(table.getBackground());
            component.setForeground(table.getForeground());
        }

        return component;
    }
}