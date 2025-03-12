package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE p.cantidad < 5 AND p.activo = 1")
    List<Producto> findProductosStockBajo();

    Producto findByNombre(String nombre);

    Optional<Producto> findById(Long id);

    @Modifying
    @Query("UPDATE Producto p SET p.nombre = :nombre, p.precio = :precio, p.cantidad = :cantidad,p.activo =:nuevoEstado WHERE p.id = :id")
    void actualizarProducto(@Param("id") Long id, @Param("nombre") String nombre,
                            @Param("precio") double precio, @Param("cantidad") int cantidad, int nuevoEstado);

    @Query("SELECT p FROM Producto p WHERE (:conInactivos = true OR p.activo = 1)")
    Page<Producto> findAll(Pageable pageable,@Param("conInactivos") boolean conInactivos);

    @Query("SELECT COUNT(p) FROM Producto p WHERE (:conInactivos = true OR p.activo = 1)")
    int getTotalNumProductos(@Param("conInactivos") boolean conInactivos);

    @Query("SELECT p FROM Producto p WHERE " +
            "(:conInactivos = true OR p.activo = 1) " +
            "AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:stock IS NULL OR p.cantidad = :stock) " +
            "AND (:precio IS NULL OR p.precio = :precio) " +
            "AND (:activo IS NULL OR p.activo = :activo)")
    Page<Producto> findFiltrado(
            @Param("nombre") String nombre,
            @Param("stock") Integer stock,
            @Param("precio") Double precio,
            @Param("activo") Integer activo,
            Pageable pageable,
            @Param("conInactivos") boolean conInactivos
    );


    @Query("SELECT COUNT(p) FROM Producto p WHERE " +
            "(:conInactivos = true OR p.activo = 1) " +
            "AND (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
            "AND (:stock IS NULL OR p.cantidad = :stock) " +
            "AND (:precio IS NULL OR p.precio = :precio) " +
            "AND (:activo IS NULL OR p.activo = :activo)")
    int getTotalNumProductos(
            @Param("nombre") String nombre,
            @Param("stock") Integer stock,
            @Param("precio") Double precio,
            @Param("activo") Integer activo,
            @Param("conInactivos") boolean conInactivos
    );

}
