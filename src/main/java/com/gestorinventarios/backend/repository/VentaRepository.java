package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COUNT(v) FROM Venta v")
    int getTotalNumeroVentas();
    @Query("SELECT COALESCE(SUM(v.cantidadVendida * p.precio), 0) FROM Venta v JOIN v.producto p")
    double getTotalIngresosVentas();
    void removeVentaById(Long id);
}
