
package com.tuempresa.sistemacitasgestion.dao;

import com.tuempresa.sistemacitasgestion.config.ConexionBD;
import com.tuempresa.sistemacitasgestion.model.CitaTecnica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitaTecnicaDAO {

    private static final String SQL_LISTAR = """
            SELECT ct.id,
                   ct.cliente_id,
                   TRIM(c.nombres || ' ' || c.apellidos) AS cliente_nombre,
                   ct.fecha,
                   ct.hora,
                   ct.tipo_servicio,
                   ct.descripcion,
                   ct.estado
            FROM cita_tecnica ct
            INNER JOIN cliente c ON c.id = ct.cliente_id
            ORDER BY ct.fecha, ct.hora
            """;

    public List<CitaTecnica> listar() throws SQLException {
        List<CitaTecnica> citas = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement ps = conexion.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }
        }

        return citas;
    }

    private CitaTecnica mapearCita(ResultSet rs) throws SQLException {
        CitaTecnica cita = new CitaTecnica();

        cita.setId(rs.getInt("id"));
        cita.setClienteId(rs.getInt("cliente_id"));
        cita.setClienteNombre(rs.getString("cliente_nombre"));
        cita.setFecha(rs.getDate("fecha").toLocalDate());
        cita.setHora(rs.getTime("hora").toLocalTime());
        cita.setTipoServicio(rs.getString("tipo_servicio"));
        cita.setDescripcion(rs.getString("descripcion"));
        cita.setEstado(rs.getString("estado"));

        return cita;
    }
}
