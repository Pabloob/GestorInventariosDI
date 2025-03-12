package com.gestorinventarios.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private double precio;
    private int cantidad;

    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private int activo = 1;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;

    public Producto(Long id, String nombre, double precio, int cantidad, int activo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.activo = activo;
    }

    public String getActivoString() {
        if (this.activo == 1) {
            return "Activo";
        }
        return "Inactivo";
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", activo=" + activo +
                ", detalles=" + detalles +
                '}';
    }
}
