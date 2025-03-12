package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.repository.DetalleVentaRepository;
import com.gestorinventarios.backend.repository.VentaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    public VentaService(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<Venta> obtenerVentas(int limitePagina, int pagina) {
        return ventaRepository.findAll(PageRequest.of(pagina, limitePagina)).getContent();
    }

    public List<Venta> obtenerVentas(LocalDate fecha,String producto, int limitePagina, int pagina) {
        return ventaRepository.findVentasPorFiltros(fecha,producto, PageRequest.of(pagina, limitePagina)).getContent();
    }

    public int obtenerNumeroVentasConDetalles(LocalDate fecha, String producto) {
        return ventaRepository.contarVentasConFiltros(fecha, producto);
    }

    @Transactional
    public void anadirVenta(Venta venta, List<DetalleVenta> detalles) {
        Venta nuevaVenta = ventaRepository.save(venta);
        detalles.forEach(detalle -> detalle.setVenta(nuevaVenta));
        detalleVentaRepository.saveAll(detalles);
    }

    @Transactional
    public boolean eliminarVenta(Long idVenta) {
        try {
            ventaRepository.deleteById(idVenta);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public double obtenerTotalIngresos() {
        return ventaRepository.getTotalIngresosVentas();
    }

    public Venta obtenerVenta(Long idVenta) {
        return ventaRepository.findById(idVenta).orElse(null);
    }

    public int obtenerNumVentas() {
        return ventaRepository.getTotalNumeroVentas();
    }
}
