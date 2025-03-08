package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByVentaId(Long ventaId);

    @Query("SELECT d.cantidad FROM DetalleVenta d WHERE d.venta.id = :ventaId AND d.producto.id = :productoId")
    int getCantidadByVentaIDandProductoId(@Param("ventaId") Long ventaId, @Param("productoId") Long productoId);

    @Query("SELECT d FROM DetalleVenta d WHERE d.venta.id = :idVenta AND EXISTS " +
            "(SELECT 1 FROM DetalleVenta dv WHERE dv.venta.id = :idVenta AND dv.producto.id = :idProducto)")
    List<DetalleVenta> findByVentaIdAndProductoId(@Param("idVenta") Long idVenta, @Param("idProducto") Long idProducto);

    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.venta.id IN " +
            "(SELECT v.id FROM Venta v WHERE v.fechaVenta = :fechaVenta)")
    List<DetalleVenta> getDetalleVentaByVentaIdFechaVenta(@Param("fechaVenta") LocalDate fechaVenta);

    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.venta.id IN " +
            "(SELECT dv2.venta.id FROM DetalleVenta dv2 " +
            "WHERE dv2.venta.fechaVenta = :fechaVenta AND dv2.producto.nombre = :nombreProducto)")
    List<DetalleVenta> obtenerDetallesPorFechaVentaYProducto(@Param("fechaVenta") LocalDate fechaVenta,
                                                             @Param("nombreProducto") String nombreProducto);
}
