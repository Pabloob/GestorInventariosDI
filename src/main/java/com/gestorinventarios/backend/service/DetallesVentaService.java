package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DetallesVentaService {
    private final DetalleVentaRepository detalleVentaRepository;

    public DetallesVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<DetalleVenta> obtenerDetallesPorVenta(Long idVenta) {
        return detalleVentaRepository.findByVentaId(idVenta);
    }
    public List<DetalleVenta> obtenerDetallesPorVentaYProducto(Long idVenta,Long idProducto) {
        return detalleVentaRepository.findByVentaIdAndProductoId(idVenta,idProducto);
    }

    public void guardarDetalleVenta(DetalleVenta detalleVenta) {
        detalleVentaRepository.save(detalleVenta);
    }

    public int obtenerCantidadPorIdVentaYProducto(Long idVenta,Long idProducto) {
        return detalleVentaRepository.getCantidadByVentaIDandProductoId(idVenta,idProducto);
    }

    public List<DetalleVenta> obtenerDetallesPorFechaVenta(LocalDate fechaVenta) {
        return detalleVentaRepository.getDetalleVentaByVentaIdFechaVenta(fechaVenta);
    }

    public List<DetalleVenta> obtenerTodosLosDetallesVenta() {
        return detalleVentaRepository.findAll();
    }

    public List<DetalleVenta> obtenerDetallesPorFechaVentaYProducto(LocalDate fechaVenta, String nombreProducto) {
        return detalleVentaRepository.obtenerDetallesPorFechaVentaYProducto(fechaVenta,nombreProducto);

    }
}
