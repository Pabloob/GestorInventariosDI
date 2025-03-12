package com.gestorinventarios.frontend.components.Tabla;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.frontend.components.BotonConEstilo;
import com.gestorinventarios.frontend.controller.DetallesVentaController;
import com.gestorinventarios.frontend.controller.ProductoController;
import com.gestorinventarios.frontend.controller.VentaController;
import com.toedter.calendar.JDateChooser;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.gestorinventarios.frontend.utils.Utiles.*;

public class SPTabla extends JScrollPane {

    //Componentes
    @Getter
    public JTable table;
    @Getter
    public DefaultTableModel tableModel;
    public final String titulo;

    //Mapeo de id y venta por fila
    Map<Integer, Long> idVentaPorFila = new HashMap<>();

    //Controladores
    public ProductoController productoController = null;
    public VentaController ventaController = null;
    public DetallesVentaController detallesVentaController = null;

    //Booleanos de configuracion
    public boolean esFiltrable = true;
    public boolean conInactivos = false;

    //Componentes panel de filtro permanentes
    public JTextField campoNombreProducto, campoStock, campoPrecio;

    //Filtro producto
    public JComboBox campoEstado;

    //Filtro venta
    public JDateChooser campoFecha;

    //Componentes y variables paginar
    public JLabel textoPagina;
    public int pagina = 1;
    public int limitePagina = 5;
    public int totalPaginas = 1;
    public int totalItems = 0;

    public Object[] filtrosAplicados = null;

    //Constructores
    public SPTabla() {
        this.titulo = "No hay titulo";
    }

    public SPTabla(String titulo, String[] columnas, Object controlador) {
        this.titulo = titulo;
        inicializarControladores(new Object[]{controlador});
        this.tableModel = new DefaultTableModel(columnas, 0);
        this.table = inicializarTabla();
        configurarScrollPanel();
    }

    public SPTabla(String titulo, String[] columnas, boolean conInactivos, Object... controladores) {
        this.titulo = titulo;
        inicializarControladores(controladores);
        this.conInactivos = conInactivos;
        this.tableModel = new DefaultTableModel(columnas, 0);
        this.table = inicializarTabla();
        configurarScrollPanel();
    }

    //Inicializar controladores con los parametros obtenidos
    private void inicializarControladores(Object[] controladores) {
        for (Object controlador : controladores) {
            if (controlador instanceof ProductoController) {
                this.productoController = (ProductoController) controlador;
            } else if (controlador instanceof VentaController) {
                this.ventaController = (VentaController) controlador;
            } else if (controlador instanceof DetallesVentaController) {
                this.detallesVentaController = (DetallesVentaController) controlador;
            }
        }
    }

    //Inicializar tabla con su configuracion
    private JTable inicializarTabla() {
        JTable tabla = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return getCellEditor(row, column) instanceof ProductoComboBoxEditor;
            }
        };
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowSorter(new TableRowSorter<>(tableModel));
        tabla.getTableHeader().setReorderingAllowed(false);
        return tabla;
    }

    //Configuracion panel
    private void configurarScrollPanel() {
        JPanel panelContenido = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);

        panelContenido.add(scrollPane, BorderLayout.CENTER);

        if (esFiltrable) {
            panelContenido.add(crearPanelFiltros(), BorderLayout.NORTH);
        }
        panelContenido.add(crearPanelPaginar(), BorderLayout.SOUTH);

        setViewportView(panelContenido);
        setBorder(BorderFactory.createTitledBorder(titulo));

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
        }
        actualizarTabla();
    }

    //Creacion panel superior de filtrado
    private JPanel crearPanelFiltros() {

        JPanel panelFiltro = new JPanel();
        BotonConEstilo botonFiltrar = new BotonConEstilo("Filtrar");

        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    botonFiltrar.doClick();
                }
            }
        };

        BotonConEstilo limpiarFiltros = new BotonConEstilo("Limpiar filtros");
        campoNombreProducto = new JTextField(5);
        campoNombreProducto.addKeyListener(enterListener);

        limpiarFiltros.addActionListener(e -> {
            actualizarTabla();
            resetearFiltro();
        });


        if (productoController != null) {
            campoStock = new JTextField(5);
            campoPrecio = new JTextField(5);

            campoStock.addKeyListener(enterListener);
            campoPrecio.addKeyListener(enterListener);

            if (conInactivos) {
                campoEstado = new JComboBox<>(new String[]{"", "Activo", "Inactivo"});
                campoEstado.addKeyListener(enterListener);
            }
            botonFiltrar.addActionListener(e -> aplicarFiltroProductos());

            panelFiltro.add(new JLabel("Nombre producto: "));
            panelFiltro.add(campoNombreProducto);
            panelFiltro.add(new JLabel("Stock: "));
            panelFiltro.add(campoStock);
            panelFiltro.add(new JLabel("Precio: "));
            panelFiltro.add(campoPrecio);
            if (conInactivos) {
                panelFiltro.add(new JLabel("Estado: "));
                panelFiltro.add(campoEstado);
            }

        } else if (ventaController != null && detallesVentaController != null) {
            campoFecha = new JDateChooser();

            campoFecha.getDateEditor().getUiComponent().addKeyListener(enterListener);

            botonFiltrar.addActionListener(e -> aplicarFiltroVentas());
            panelFiltro.add(new JLabel("Nombre producto: "));
            panelFiltro.add(campoNombreProducto);
            panelFiltro.add(new JLabel("Fecha: "));
            panelFiltro.add(campoFecha);
        }

        panelFiltro.add(botonFiltrar);
        panelFiltro.add(limpiarFiltros);

        return panelFiltro;
    }

    //Creacion de panel inferior de paginacion
    private JPanel crearPanelPaginar() {
        if (productoController != null) {
            totalItems = productoController.obtenerNumProductos(null, conInactivos);
        } else {
            totalItems = ventaController.obtenerNumeroVentasFiltradas(filtrosAplicados);
        }

        totalPaginas = Math.max(1, (int) Math.ceil((double) totalItems / limitePagina));

        JPanel panelPaginado = new JPanel();
        BotonConEstilo anterior = new BotonConEstilo("<");
        BotonConEstilo siguiente = new BotonConEstilo(">");
        JLabel textoCB = new JLabel("Limite columnas: ");
        textoPagina = new JLabel("Pagina actual: " + pagina + " / " + totalPaginas);
        JComboBox<Integer> limitesPagina = new JComboBox<>(new Integer[]{1, 5, 10, 20, 30});
        limitesPagina.setSelectedItem(5);

        anterior.addActionListener(e -> {
            if (pagina > 1) {
                pagina--;
                actualizarTabla();
                textoPagina.setText("Pagina actual: " + pagina + " / " + totalPaginas);
            }
        });

        siguiente.addActionListener(e -> {
            if (pagina < totalPaginas) {
                pagina++;
                actualizarTabla();
                textoPagina.setText("Pagina actual: " + pagina + " / " + totalPaginas);
            }
        });

        limitesPagina.addActionListener(e -> {
            limitePagina = (int) limitesPagina.getSelectedItem();
            recalcularPaginacion();
            actualizarTabla();
            resetearFiltro();
        });

        panelPaginado.add(anterior);
        panelPaginado.add(textoPagina);
        panelPaginado.add(siguiente);
        panelPaginado.add(textoCB);
        panelPaginado.add(limitesPagina);
        return panelPaginado;
    }

    //Metodo resetear filtros
    private void resetearFiltro() {
        filtrosAplicados = null;
        Arrays.asList(campoNombreProducto, campoStock, campoPrecio, campoNombreProducto).forEach(c -> {
            if (c != null) {
                c.setText("");
            }
        });
        if (campoEstado != null) {
            campoEstado.setSelectedIndex(0);
        }
        if (campoFecha != null) {
            campoFecha.setDate(null);
        }
        recalcularPaginacion();
        actualizarTabla();
    }

    //Aplicar filtro para productos
    private void aplicarFiltroProductos() {
        filtrosAplicados = new Object[]{
                obtenerTextoCampo(campoNombreProducto),
                validarInt(campoStock.getText().trim()),
                validarDouble(campoPrecio.getText().trim()),
                campoEstado != null ? campoEstado.getSelectedItem() : null
        };
        recalcularPaginacion();
        actualizarTabla();
    }

    //Aplicar filtro para ventas
    private void aplicarFiltroVentas() {
        filtrosAplicados = new Object[]{
                campoFecha.getDate() != null ? campoFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null,
                obtenerTextoCampo(campoNombreProducto)
        };
        recalcularPaginacion();
        actualizarTabla();
    }

    //Calcular paginas e items
    private void recalcularPaginacion() {
        if (productoController != null) {
            totalItems = productoController.obtenerNumProductos(filtrosAplicados, conInactivos);
        } else {
            totalItems = ventaController.obtenerNumeroVentasFiltradas(filtrosAplicados);
        }
        totalPaginas = Math.max(1, (int) Math.ceil((double) totalItems / limitePagina));
        pagina = 1;
        textoPagina.setText("Pagina actual: " + pagina + " / " + totalPaginas);
    }

    //Metodo comun para actualizar tablas
    public void actualizarTabla() {
        if (productoController != null) {
            actualizarTablaProductos(conInactivos);
        } else {
            actualizarTablaVentas();
        }
    }

    //Actualizar tabla de ventas
    private void actualizarTablaVentas() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);

            Map<Integer, String[]> productosPorFila = new HashMap<>();
            idVentaPorFila.clear();
            // Obtener las ventas con o sin filtro de fecha
            List<Venta> ventas = Optional.ofNullable(filtrosAplicados)
                    .map(f -> ventaController.obtenerVentas(f, limitePagina, Math.max(0, pagina - 1)))
                    .orElse(ventaController.obtenerVentas(limitePagina, Math.max(0, pagina - 1)));

            int rowIndex = 0;
            for (Venta venta : ventas) {
                List<DetalleVenta> detalles = venta.getDetalles();

                // Extraer productos y cantidades
                String[] productos = detalles.stream()
                        .map(detalle -> detalle.getProducto().getNombre())
                        .toArray(String[]::new);

                Map<String, Integer> productoCantidadMap = detalles.stream()
                        .collect(Collectors.toMap(detalle -> detalle.getProducto().getNombre(), DetalleVenta::getCantidad, Integer::sum));

                String productoSeleccionado = productos.length > 0 ? productos[0] : "";
                int cantidadInicial = productoCantidadMap.getOrDefault(productoSeleccionado, 0);

                // Guardar productos en el mapa para el ComboBox
                productosPorFila.put(rowIndex, productos);

                idVentaPorFila.put(rowIndex, venta.getId());

                // Calcular el total correctamente
                double totalVenta = detalles.stream().mapToDouble(detalle -> detalle.getCantidad() * detalle.getPrecioUnitario()).sum();

                // Agregar fila a la tabla
                getTableModel().addRow(new Object[]{
                        productoSeleccionado,
                        cantidadInicial,
                        String.format("%.2f€", totalVenta),
                        venta.getFechaVenta(),
                        venta.getId()
                });
                rowIndex++;
            }

            // Asignar el editor y renderizador del ComboBox
            table.getColumnModel().getColumn(0).setCellEditor(new ProductoComboBoxEditor(productosPorFila, detallesVentaController, this));
        });
    }

    //Actualizar tabla productos
    private void actualizarTablaProductos(boolean incluirInactivos) {

        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Producto> productos = productoController.obtenerProductos(filtrosAplicados, limitePagina, Math.max(0, pagina - 1), incluirInactivos);
            productos.forEach(p -> {
                if (incluirInactivos) {
                    tableModel.addRow(new Object[]{p.getNombre(), p.getCantidad(), String.format("%.2f€", p.getPrecio()), p.getActivoString()});
                } else {
                    tableModel.addRow(new Object[]{p.getNombre(), p.getCantidad(), String.format("%.2f€", p.getPrecio())});
                }
            });
        });
    }

    //Setter de si la tabla se puede filtrar
    public void setEsFiltrable(boolean esFiltrable) {
        this.esFiltrable = esFiltrable;
        inicializarTabla();
    }

    //Obtener id de venta por fila
    public long getVentaId(int row) {
        if (ventaController != null) {
            return idVentaPorFila.getOrDefault(row, 0L);
        }
        return -1;
    }

    //Obtener y validar texto
    private String obtenerTextoCampo(JTextField campo) {
        String texto = campo.getText().trim();
        return texto.isEmpty() ? null : texto;
    }
}
