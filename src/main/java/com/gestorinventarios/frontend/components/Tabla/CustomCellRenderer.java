package com.gestorinventarios.frontend.components.Tabla;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Obtener el nombre de la columna
        String columnName = table.getColumnName(column);

        // Aplicar alineación centrada para mejorar la estética
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplicar efectos visuales para la columna "Stock"
        if ("Stock".equalsIgnoreCase(columnName) && value instanceof Number) {
            int stockValue = ((Number) value).intValue();

            if (stockValue <= 5) {
                // Si el stock es muy bajo, usar un degradado rojo-anaranjado
                label.setBackground(new Color(255, 69, 58)); // Rojo fuerte
                label.setBorder(BorderFactory.createLineBorder(new Color(180, 0, 0), 1, true)); // Borde sutil
            } else if (stockValue <= 10) {
                // Stock entre 6-10: degradado más suave
                label.setBackground(new Color(255, 140, 0)); // Naranja
                label.setBorder(BorderFactory.createLineBorder(new Color(255, 69, 0), 1, true));
            } else {
                // Stock normal: fondo blanco con texto oscuro
                label.setBackground(table.getBackground());
                label.setBorder(null);
            }
        } else {
            label.setBorder(null);
        }

        return label;
    }
}
