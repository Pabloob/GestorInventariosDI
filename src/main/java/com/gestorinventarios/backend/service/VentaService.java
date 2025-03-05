package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;

    public int obtenerNumVentas(){
        return ventaRepository.getTotalNumeroVentas();
    };

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    public double obtenerTotalIngresos() {
        return ventaRepository.getTotalIngresosVentas();
    }

    public void eliminarVenta(long idVenta) {
        ventaRepository.removeVentaById(idVenta);
    }
}
