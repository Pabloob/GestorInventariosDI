package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE p.cantidad < 5")
    List<Producto> findProductosStockBajo();

    Producto findByNombre(String nombre);

    Optional<Producto> findById(Long id);

}
