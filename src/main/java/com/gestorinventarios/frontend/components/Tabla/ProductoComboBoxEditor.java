package com.gestorinventarios.frontend.components.Tabla;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class ProductoComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
    private final JComboBox<String> comboBox;
    public ProductoComboBoxEditor() {
        comboBox = new JComboBox<>();
        comboBox.setBackground(new Color(100, 149, 237));
        comboBox.setForeground(Color.WHITE);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (table.getColumnCount() == 5) {
            String[] productos = (String[]) table.getModel().getValueAt(row, 5);
            comboBox.removeAllItems();
            for (String producto : productos) {
                comboBox.addItem(producto);
            }
            comboBox.setSelectedItem(value);
            return comboBox;
        }
        return null;
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }
}