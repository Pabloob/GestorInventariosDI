package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT COALESCE(SUM(dv.cantidad * dv.precioUnitario), 0) FROM DetalleVenta dv")
    double getTotalIngresosVentas();

    @Query("SELECT COUNT(v) FROM Venta v")
    int getTotalNumeroVentas();

    Page<Venta> findAll(Pageable pageable);

    @Query("""
        SELECT v FROM Venta v
        WHERE (:fecha IS NULL OR v.fechaVenta = :fecha)
        AND (:producto IS NULL OR EXISTS (
            SELECT dv FROM DetalleVenta dv
            WHERE dv.venta.id = v.id AND dv.producto.nombre = :producto
        ))
    """)
    Page<Venta> findVentasPorFiltros(@Param("fecha") LocalDate fecha,
                                     @Param("producto") String producto,
                                     Pageable pageable);

    @Query("""
        SELECT COUNT(DISTINCT v) FROM Venta v JOIN DetalleVenta d ON d.venta.id = v.id
        WHERE (:fecha IS NULL OR v.fechaVenta = :fecha)
        AND (:producto IS NULL OR d.producto.nombre = :producto)
    """)
    int contarVentasConFiltros(@Param("fecha") LocalDate fecha, @Param("producto") String producto);

}
