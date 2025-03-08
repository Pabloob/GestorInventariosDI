package com.gestorinventarios.frontend.components.Tabla;

import com.gestorinventarios.frontend.controller.DetallesVentaController;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.*;

public class ProductoComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
    private final JComboBox<String> comboBox;
    private final Map<Integer, String[]> productosPorFila;

    public ProductoComboBoxEditor(Map<Integer, String[]> productosPorFila,
                                  DetallesVentaController detallesVentaController,
                                  JTable tabla) {
        this.productosPorFila = productosPorFila;

        comboBox = new JComboBox<>();
        comboBox.setBackground(new Color(100, 149, 237));
        comboBox.setForeground(Color.WHITE);

        comboBox.addActionListener(e -> {
            String selectedItem = (String) comboBox.getSelectedItem();
            if (selectedItem != null) {
                int row = tabla.getEditingRow();
                if (row > 0 && row < tabla.getRowCount()) {
                    long idVenta = (long) tabla.getValueAt(row, 0);
                    int cantidad = detallesVentaController.obtenerCantidadPorIdVentaYProducto(idVenta, selectedItem);
                    tabla.setValueAt(cantidad, row, 2);
                    stopCellEditing();
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        comboBox.removeAllItems();

        String[] productos = productosPorFila.get(row);
        for (String producto : productos) {
            comboBox.addItem(producto);
        }

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
