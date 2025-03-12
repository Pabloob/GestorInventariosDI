package com.gestorinventarios.frontend.controller;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Controller
public class ProductoController {
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    public int obtenerNumProductos(boolean conInactivos) {
        return productoService.obtenerNumProductos(conInactivos);
    }

    public int obtenerNumProductos(Object[] filtros, boolean conInactivos) {
        if (filtros == null) {
            return obtenerNumProductos(conInactivos);
        }
        return productoService.obtenerNumProductos(procesarFiltros(filtros), conInactivos);
    }

    public List<Producto> obtenerProductos(int limitePagina, int pagina, boolean conInactivos) {
        return productoService.obtenerProductos(limitePagina, pagina, conInactivos);
    }

    public List<Producto> obtenerProductos(Object[] filtros, int limitePagina, int pagina, boolean conInactivos) {
        if (filtros == null) {
            return obtenerProductos(limitePagina, pagina, conInactivos);
        }
        return productoService.obtenerProductos(procesarFiltros(filtros), limitePagina, pagina, conInactivos);
    }

    private Object[] procesarFiltros(Object[] filtros) {
        Object[] filtrosProcesados = Arrays.copyOf(filtros, filtros.length);

        if (filtrosProcesados[3] instanceof String) {
            String estado = ((String) filtrosProcesados[3]).trim().toLowerCase();
            filtrosProcesados[3] = switch (estado) {
                case "activo" -> 1;
                case "inactivo" -> 0;
                default -> null;
            };
        } else if (!(filtrosProcesados[3] instanceof Integer)) {
            filtrosProcesados[3] = null;
        }

        return filtrosProcesados;
    }

    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    public Producto obtenerPorNombre(String nombre) {
        return productoService.obtenerProducto(nombre);
    }

    public boolean anadirProducto(String nombre, double precio, int cantidad) {
        return productoService.anadirProducto(new Producto(null, nombre, precio, cantidad, 1, null));
    }

    public boolean actualizarProducto(Long id, String nombre, double precio, int cantidad, int nuevoEstado) {
        return productoService.actualizarProducto(id, nombre, precio, cantidad, nuevoEstado);
    }

    public boolean eliminarProducto(Long id) {
        return productoService.eliminarProducto(id);
    }

    public int obtenerStockBajo() {
        return productoService.obtenerStockBajo();
    }

    public boolean existeStock(List<DetalleVenta> detalles) {
        for (DetalleVenta detalle : detalles) {
            Producto producto = detalle.getProducto();
            if (producto.getCantidad() >= detalle.getCantidad()) {
                producto.setCantidad(producto.getCantidad() - detalle.getCantidad());
                actualizarProducto(producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getCantidad(),
                        producto.getActivo()
                );
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean existeStock(String nombre, int cantidad) {
        return productoService.obtenerProducto(nombre).getCantidad() >= cantidad;
    }
}

