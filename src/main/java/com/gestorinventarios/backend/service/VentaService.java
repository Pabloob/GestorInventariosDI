package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.repository.DetalleVentaRepository;
import com.gestorinventarios.backend.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public VentaService(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public int obtenerNumVentas() {
        return ventaRepository.getTotalNumeroVentas();
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta guardarVenta(Venta venta, List<DetalleVenta> detalles) {
        Venta nuevaVenta = ventaRepository.save(venta);
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(nuevaVenta);
            detalleVentaRepository.save(detalle);
        }
        return nuevaVenta;
    }

    public double obtenerTotalIngresos() {
        return ventaRepository.getTotalIngresosVentas();
    }

    public void eliminarVenta(Long idVenta) {
        ventaRepository.deleteById(idVenta);
    }
}
