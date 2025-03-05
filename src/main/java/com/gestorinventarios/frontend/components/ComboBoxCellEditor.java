package com.gestorinventarios.frontend.components;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.EventObject;

public class ComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JComboBox<String> comboBox;
    private String[] currentItems;

    public ComboBoxCellEditor() {
        comboBox = new JComboBox<>();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String[] && ((String[]) value).length > 1) {
            currentItems = (String[]) value;
            comboBox.setModel(new DefaultComboBoxModel<>(currentItems));
            return comboBox;
        }
        return new JLabel(value != null ? value.toString() : "");
    }

    @Override
    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}

