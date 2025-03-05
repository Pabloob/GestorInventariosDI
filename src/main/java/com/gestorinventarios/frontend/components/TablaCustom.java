package com.gestorinventarios.frontend.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

@Getter
@Setter
public class TablaCustom extends JScrollPane {
    private JTable table;
    private DefaultTableModel tableModel;

    public TablaCustom(String titulo, String[] columnas) {
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setForeground(UIManager.getColor("Label.foreground"));

        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        table.setBackground(UIManager.getColor("Table.background"));
        table.setForeground(UIManager.getColor("Table.foreground"));
        table.setGridColor(UIManager.getColor("Table.gridColor"));

        setViewportView(table);
        setBorder(BorderFactory.createTitledBorder(titulo));

        RendererTablaCustom renderer = new RendererTablaCustom();
        for (int i = 0; i < columnas.length; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
}