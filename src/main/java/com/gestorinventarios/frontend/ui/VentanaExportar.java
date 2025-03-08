package com.gestorinventarios.frontend.ui;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class VentanaExportar extends BaseView {
    private JButton exportarProductos;
    private JButton exportarVentas;
    private TablaCustom tablaProductos;
    private TablaCustom tablaVentas;

    public VentanaExportar() {
        super("Exportar", 1200, 700);

        crearTitulo("Exportar");

        tablaVentas = new TablaCustom("Ventas", new String[]{"ID", "Producto", "Cantidad", "Precio", "Fecha"}, ventaController, detallesVentaController);
        tablaProductos = new TablaCustom("Productos", new String[]{"Nombre", "Stock", "Precio"}, productoController);
        tablaVentas.setEsEditable(false);
        tablaProductos.setEsEditable(false);

        crearPanelTablas(1, 1, tablaProductos, tablaVentas);
        exportarProductos = crearBoton("Exportar Productos");
        exportarVentas = crearBoton("Exportar Ventas");

        crearPanelBotones(2, exportarProductos, exportarVentas);

        // Asignar acción a los botones
        exportarProductos.addActionListener(e -> exportarReporte("productos"));
        exportarVentas.addActionListener(e -> exportarReporte("ventas"));
    }

    private void exportarReporte(String tipo) {
        // Seleccionar el reporte según el tipo
        String reportePath = "./src/main/java/com/gestorinventarios/Reportes/";
        String reporteArchivo = tipo.equals("productos") ? "ReporteProductosTodos.jasper" : "ReporteVentasTodos.jasper";
        String reporteCompleto = reportePath + reporteArchivo;

        // Opciones de formato de exportación
        String[] opciones = {"PDF", "HTML", "XML", "Excel (XLS)"};
        String seleccion = (String) JOptionPane.showInputDialog(
                null, "Seleccione el formato de exportación:", "Exportar Reporte",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == null) return; // Si el usuario cancela, salir

        // Selector de archivos para elegir la ubicación
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        fileChooser.setSelectedFile(new File("Reporte_" + tipo + "." + seleccion.toLowerCase()));

        int seleccionUsuario = fileChooser.showSaveDialog(null);
        if (seleccionUsuario != JFileChooser.APPROVE_OPTION) return;

        File archivoSalida = fileChooser.getSelectedFile();
        String rutaSalida = archivoSalida.getAbsolutePath();

        try {
            // Conectar con SQLite
            Connection conexion = DriverManager.getConnection("jdbc:sqlite:src/main/database/gestor_inventarios.db");

            // Cargar el reporte
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(reporteCompleto);

            // Parámetros del reporte
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("NombreEmpresa", "PabloSA");

            // Llenar el reporte
            JasperPrint miInforme = JasperFillManager.fillReport(reporte, parametros, conexion);

            // Exportar en el formato seleccionado
            switch (seleccion) {
                case "PDF":
                    JasperExportManager.exportReportToPdfFile(miInforme, rutaSalida);
                    break;
                case "HTML":
                    JasperExportManager.exportReportToHtmlFile(miInforme, rutaSalida);
                    break;
                case "XML":
                    JasperExportManager.exportReportToXmlFile(miInforme, rutaSalida, false);
                    break;
                case "Excel (XLS)":
                    JRXlsExporter exporterXLS = new JRXlsExporter();
                    exporterXLS.setExporterInput(new SimpleExporterInput(miInforme));
                    exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(rutaSalida + ".xls"));
                    exporterXLS.exportReport();
                    break;
            }

            conexion.close();

            JOptionPane.showMessageDialog(null, "Reporte exportado correctamente en " + seleccion, "Éxito", JOptionPane.INFORMATION_MESSAGE);

            JasperViewer.viewReport(miInforme, false);
        } catch (SQLException | JRException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al exportar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
