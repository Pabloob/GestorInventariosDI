package com.gestorinventarios.backend.repository;

import com.gestorinventarios.backend.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:src/main/database/gestor_inventarios.db"); // Ajusta la conexión a tu BD
    }

    public List<Producto> obtenerFiltrado(Object[] filtros) {
        List<Producto> productos = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM producto WHERE 1=1 ");
        List<Object> parametros = new ArrayList<>();

        // Aplicar filtros dinámicamente
        if (filtros[0] != null) { // Nombre
            query.append(" AND LOWER(nombre) LIKE LOWER(?) ");
            parametros.add("%" + filtros[0] + "%");
        }
        if (filtros[1] != null) { // Stock mínimo
            query.append(" AND cantidad = ? ");
            parametros.add(filtros[1]);
        }
        if (filtros[2] != null) { // Precio máximo
            query.append(" AND precio = ? ");
            parametros.add(filtros[2]);
        }

        query.append(" ORDER BY nombre ASC"); // Ordenar por nombre


        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query.toString())) {
            // Asignar parámetros
            for (int i = 0; i < parametros.size(); i++) {
                ps.setObject(i + 1, parametros.get(i));
            }
            System.out.println(ps.toString());
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(new Producto(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("cantidad"),
                            rs.getInt("activo")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }
}
