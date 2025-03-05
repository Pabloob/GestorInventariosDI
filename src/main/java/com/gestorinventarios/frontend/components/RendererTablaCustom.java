package com.gestorinventarios.frontend.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class RendererTablaCustom extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (column == 1 && value instanceof String[] && ((String[]) value).length > 1) {
            JComboBox<String> comboBox = new JComboBox<>((String[]) value);
            return comboBox;
        }

        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);

        int stockColumnIndex = obtenerIndiceColumna(table, "Stock");

        if (stockColumnIndex != -1) {
            Object stockValue = table.getModel().getValueAt(row, stockColumnIndex);
            if (stockValue instanceof Integer && (Integer) stockValue < 5) {
                cell.setBackground(new Color(255, 69, 58));
                cell.setForeground(Color.WHITE);
                return cell;
            }
        }

        if (!isSelected) {
            if (UIManager.getLookAndFeel().getName().contains("Dark")) {
                cell.setBackground(row % 2 == 0 ? new Color(60, 60, 60) : new Color(85, 85, 85));
                cell.setForeground(Color.WHITE);
            } else {
                cell.setBackground(row % 2 == 0 ? new Color(240, 240, 240) : Color.WHITE);
                cell.setForeground(Color.BLACK);
            }
        } else {
            cell.setBackground(new Color(100, 149, 237));
            cell.setForeground(Color.WHITE);
        }

        return cell;
    }


    private int obtenerIndiceColumna(JTable table, String columnName) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }
}