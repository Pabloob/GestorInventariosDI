package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COUNT(v) FROM Venta v")
    int getTotalNumeroVentas();

    @Query("SELECT COALESCE(SUM(dv.cantidad * dv.precioUnitario), 0) FROM DetalleVenta dv WHERE dv.venta.id = :ventaId")
    double getTotalIngresosPorVenta(Long ventaId);

    @Query("SELECT COALESCE(SUM(dv.cantidad * dv.precioUnitario), 0) FROM DetalleVenta dv")
    double getTotalIngresosVentas();

    List<Venta> findByFechaVenta(LocalDate fechaVenta);
}
