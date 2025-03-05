package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;

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

    public void guardarDetalleVenta(DetalleVenta detalleVenta) {
        detalleVentaRepository.save(detalleVenta);
    }
}
