package com.gestorinventarios.frontend.components.Tabla;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class TablaCustom extends JScrollPane {
    private JTable table;
    private DefaultTableModel tableModel;

    public TablaCustom(String titulo, String[] columnas) {
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(UIManager.getColor("Label.foreground"));

        // Crear el modelo de tabla
        tableModel = new DefaultTableModel(columnas, 0);

        // Crear la tabla
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                TableCellEditor editor = getCellEditor(row, column);
                return editor instanceof ProductoComboBoxEditor;

            }
        };

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.setBackground(UIManager.getColor("Table.background"));
        table.setForeground(UIManager.getColor("Table.foreground"));
        table.setGridColor(UIManager.getColor("Table.gridColor"));

        table.getColumnModel().getColumn(1).setCellEditor(new ProductoComboBoxEditor());
        table.getColumnModel().getColumn(1).setCellRenderer(new ProductoComboBoxRenderer());

        setViewportView(table);
        setBorder(BorderFactory.createTitledBorder(titulo));
    }

    public JTable getTable() {
        return table;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}