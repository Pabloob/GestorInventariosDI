package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVentaId(Long ventaId);

    @Query("SELECT d.cantidad FROM DetalleVenta d WHERE d.venta.id = :ventaId AND d.producto.id = :productoId")
    int getCantidadByVentaIDandProductoId(@Param("ventaId") Long ventaId, @Param("productoId") Long productoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM DetalleVenta d WHERE d.venta = :venta")
    void eliminarDetallesPorVenta(@Param("venta") Venta venta);

}
