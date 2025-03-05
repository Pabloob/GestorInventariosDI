package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.Producto;
import com.gestorinventarios.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = productoRepository.findById(id).orElse(null);
        producto.setActivo(0);
        productoRepository.save(producto);
    }

    public int obtenerStockBajo() {
        return productoRepository.findProductosStockBajo().size();
    }

    public Producto obtenerPorNombre(String nombre) {
        return productoRepository.findByNombre(nombre);
    }
}
