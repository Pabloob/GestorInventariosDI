package com.gestorinventarios.frontend.ui.menus;

import com.gestorinventarios.frontend.components.BaseView;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.components.Tabla.SPTabla;
import com.gestorinventarios.frontend.ui.VentanaPrincipal;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.gestorinventarios.frontend.utils.Utiles.mostrarMensajeError;
import static com.gestorinventarios.frontend.utils.Utiles.mostrarMensajeInformacion;

public class VentanaExportar extends BaseView {
    private final BotonConEstilo exportarProductos, exportarVentas;
    private final SPTabla tablaVentas, tablaProductos;

    public VentanaExportar() {
        super("Exportar", 1400, 700);

        crearTitulo("Exportar");

        tablaVentas = new SPTabla("Ventas", new String[]{"ID", "Producto", "Cantidad", "Precio", "Fecha"}, false, ventaController, detallesVentaController);
        tablaProductos = new SPTabla("Productos", new String[]{"Nombre", "Stock", "Precio"}, productoController);

        tablaVentas.setEsFiltrable(false);
        tablaProductos.setEsFiltrable(false);

        crearPanelComponentes(1, 1, tablaProductos, tablaVentas);
        exportarProductos = new BotonConEstilo("Exportar Productos", () -> exportarReporte("productos"));
        exportarVentas = new BotonConEstilo("Exportar Ventas", () -> exportarReporte("ventas"));

        crearPanelBotones(2, exportarProductos, exportarVentas);
        crearMenu();
    }
    private void exportarReporte(String tipo) {
        String reportePath = "./src/main/java/com/gestorinventarios/Reportes/";
        String reporteArchivo = tipo.equals("productos") ? "ReporteProductosTodos.jasper" : "ReporteVentasTodos.jasper";
        String reporteCompleto = reportePath + reporteArchivo;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reporte");
        fileChooser.setSelectedFile(new File("Reporte_" + tipo + ".pdf"));

        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;
        File archivoSalida = fileChooser.getSelectedFile();
        String rutaSalida = archivoSalida.getAbsolutePath();

        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:src/main/database/gestor_inventarios.db")) {
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(reporteCompleto);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("NombreEmpresa", "PabloSA");

            JasperPrint miInforme = JasperFillManager.fillReport(reporte, parametros, conexion);

            JasperExportManager.exportReportToPdfFile(miInforme, rutaSalida);

            mostrarMensajeInformacion(this, "Reporte exportado correctamente", "Éxito");
            JasperViewer.viewReport(miInforme, false);
        } catch (SQLException | JRException e) {
            mostrarMensajeError(this, "Error al exportar el reporte: " + e.getMessage(), "Error");
        }
    }
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        JMenuItem ventanaPrincipal = new JMenuItem("Ventana principal");
        JMenuItem ventas = new JMenuItem("Ventas");
        JMenuItem productos = new JMenuItem("Productos");
        JMenuItem configuracion = new JMenuItem("Configuracion");
        menu.add(ventanaPrincipal);
        menu.add(ventas);
        menu.add(configuracion);
        menu.add(productos);
        menu.add(añadirMenuAyuda());
        menuBar.add(menu);
        setJMenuBar(menuBar);

        ventanaPrincipal.addActionListener(e -> new VentanaPrincipal());
        ventas.addActionListener(e -> new VentanaVentas());
        configuracion.addActionListener(e -> new VentanaConfiguracion());
        productos.addActionListener(e -> new VentanaProductos());
    }

}
