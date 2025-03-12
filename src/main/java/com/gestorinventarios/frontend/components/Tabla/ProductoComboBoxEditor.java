package com.gestorinventarios.frontend.components.Tabla;

import com.gestorinventarios.frontend.controller.DetallesVentaController;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.*;

public class ProductoComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
    private final JComboBox<String> comboBox;
    private final Map<Integer, String[]> productosPorFila;

    public ProductoComboBoxEditor(Map<Integer, String[]> productosPorFila, DetallesVentaController detallesVentaController, SPTabla tabla) {
        this.productosPorFila = productosPorFila;
        comboBox = new JComboBox<>();

        comboBox.setBackground(new Color(100, 149, 237));
        comboBox.setForeground(Color.WHITE);

        comboBox.addActionListener(e -> {
            String selectedItem = (String) comboBox.getSelectedItem();
            int row = tabla.getTable().getEditingRow();
            if (selectedItem != null && row >= 0) {
                long idVenta = tabla.getVentaId(row);
                int cantidad = detallesVentaController.obtenerCantidadPorIdVentaYProducto(idVenta, selectedItem);
                tabla.getTable().setValueAt(cantidad, row, 1);
                stopCellEditing();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.removeAllItems();
        Arrays.stream(productosPorFila.get(row)).forEach(comboBox::addItem);
        comboBox.setSelectedItem(value);
        return comboBox;
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

