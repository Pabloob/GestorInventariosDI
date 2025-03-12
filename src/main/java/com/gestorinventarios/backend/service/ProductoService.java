package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.repository.ProductoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public int obtenerNumProductos(boolean conInactivos) {
        return productoRepository.getTotalNumProductos(conInactivos);
    }

    public int obtenerNumProductos(Object[] filtros, boolean conInactivos) {
        String nombre = filtros[0] != null ? filtros[0].toString() : null;
        Integer stock = (filtros[1] instanceof Integer && (int) filtros[1] > 0) ? (Integer) filtros[1] : null;
        Double precio = (filtros[2] instanceof Double && (double) filtros[2] > 0) ? (Double) filtros[2] : null;

        Integer activo = null;
        if (filtros[3] instanceof String) {
            String estado = ((String) filtros[3]).trim().toLowerCase();
            if (estado.equals("activo")) activo = 1;
            else if (estado.equals("inactivo")) activo = 0;
        } else if (filtros[3] instanceof Integer) {
            activo = (Integer) filtros[3];
        }

        return productoRepository.getTotalNumProductos(nombre, stock, precio, activo, conInactivos);
    }

    public List<Producto> obtenerProductos(int limitePagina, int pagina, boolean conInactivos) {
        PageRequest pageable = PageRequest.of(Math.max(0, pagina), limitePagina);
        return productoRepository.findAll(pageable, conInactivos).getContent();
    }

    public List<Producto> obtenerProductos(Object[] filtros, int limitePagina, int pagina, boolean conInactivos) {
        String nombre = filtros[0] != null ? filtros[0].toString() : null;
        Integer stock = (filtros[1] instanceof Integer && (Integer) filtros[1] >= 0) ? (Integer) filtros[1] : null;
        Double precio = (filtros[2] instanceof Double && (Double) filtros[2] >= 0) ? (Double) filtros[2] : null;

        Integer activo = null;
        if (filtros[3] instanceof String) {
            String estado = ((String) filtros[3]).trim().toLowerCase();
            if (estado.equals("activo")) activo = 1;
            else if (estado.equals("inactivo")) activo = 0;
        } else if (filtros[3] instanceof Integer) {
            activo = (Integer) filtros[3];
        }
        return productoRepository.findFiltrado(nombre, stock, precio, activo, PageRequest.of(Math.max(0, pagina), limitePagina), conInactivos).getContent();
    }

    public List<Producto> obtenerProductos() {
        return productoRepository.findAll();
    }

    public int obtenerStockBajo() {
        return productoRepository.findProductosStockBajo().size();
    }

    public Producto obtenerProducto(String nombre) {
        return productoRepository.findByNombre(nombre);
    }

    @Transactional
    public boolean anadirProducto(Producto producto) {
        try {
            productoRepository.save(producto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean actualizarProducto(Long id, String nombre, double precio, int cantidad, int nuevoEstado) {
        try {
            productoRepository.actualizarProducto(id, nombre, precio, cantidad, nuevoEstado);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean eliminarProducto(Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setActivo(0);
                    productoRepository.save(producto);
                    return true;
                })
                .orElse(false);
    }


}
