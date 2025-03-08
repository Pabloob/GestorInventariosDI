package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.service.ProductoService;
import com.gestorinventarios.frontend.components.Tabla.TablaCustom;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.table.DefaultTableModel;
import java.util.List;

@Controller
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    public List<Producto> obtenerProductos() {
        return productoService.listarProductos();
    }
    public List<Producto> obtenerProductos(Object [] filtros) {
        return productoService.listarProductos(filtros);
    }

    public Producto obtenerPorNombre(String nombre) {
        return productoService.obtenerPorNombre(nombre);
    }

    public void registrarProducto(String nombre, double precio, int cantidad) {
        Producto producto = new Producto(null, nombre, precio, cantidad, 1, null);
        productoService.guardarProducto(producto);
    }

    @Transactional
    public void eliminarProducto(Long id) {
        productoService.eliminarProducto(id);
    }

    public int obtenerStockBajo() {
        return productoService.obtenerStockBajo();
    }

}
