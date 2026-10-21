
package com.tuempresa.sistemacitasgestion.dao;

import com.tuempresa.sistemacitasgestion.config.ConexionBD;
import com.tuempresa.sistemacitasgestion.model.CitaTecnica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class CitaTecnicaDAO {

    private static final String SELECT =
            "SELECT ct.id, ct.cliente_id, " +
                    "TRIM(c.nombres || ' ' || c.apellidos) AS cliente_nombre, " +
                    "ct.fecha, ct.hora, ct.tipo_servicio, " +
                    "ct.descripcion, ct.estado " +
                    "FROM cita_tecnica ct " +
                    "INNER JOIN cliente c ON c.id = ct.cliente_id";

    public List<CitaTecnica> listar() throws SQLException {
        String sql = SELECT + " ORDER BY ct.fecha, ct.hora";
        List<CitaTecnica> lista = new ArrayList<>();

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }

        return lista;
    }

    public List<CitaTecnica> filtrar(String estado, LocalDate fecha)
            throws SQLException {

        StringBuilder sql = new StringBuilder(SELECT);
        List<CitaTecnica> lista = new ArrayList<>();

        boolean tieneEstado = estado != null && !estado.isBlank();
        boolean tieneFecha = fecha != null;

        if (tieneEstado || tieneFecha) {
            sql.append(" WHERE ");
        }

        if (tieneEstado) {
            sql.append("ct.estado = ?");
        }

        if (tieneEstado && tieneFecha) {
            sql.append(" AND ");
        }

        if (tieneFecha) {
            sql.append("ct.fecha = ?");
        }

        sql.append(" ORDER BY ct.fecha, ct.hora");

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql.toString())) {

            int parametro = 1;

            if (tieneEstado) {
                ps.setString(parametro++, estado.trim());
            }

            if (tieneFecha) {
                ps.setDate(parametro, Date.valueOf(fecha));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }

        return lista;
    }

    public void insertar(CitaTecnica cita) throws SQLException {
        String sql = "INSERT INTO cita_tecnica " +
                "(cliente_id, fecha, hora, tipo_servicio, descripcion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            establecerParametros(ps, cita);
            ps.executeUpdate();
        }
    }

    public void actualizar(CitaTecnica cita) throws SQLException {
        String sql = "UPDATE cita_tecnica SET " +
                "cliente_id = ?, fecha = ?, hora = ?, " +
                "tipo_servicio = ?, descripcion = ?, estado = ? " +
                "WHERE id = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            establecerParametros(ps, cita);
            ps.setInt(7, cita.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM cita_tecnica WHERE id = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public boolean existeCliente(int clienteId) throws SQLException {
        String sql = "SELECT 1 FROM cliente WHERE id = ?";

        try (Connection cn = ConexionBD.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, clienteId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void establecerParametros(
            PreparedStatement ps, CitaTecnica cita) throws SQLException {

        ps.setInt(1, cita.getClienteId());

        if (cita.getFecha() == null) {
            ps.setNull(2, Types.DATE);
        } else {
            ps.setDate(2, Date.valueOf(cita.getFecha()));
        }

        if (cita.getHora() == null) {
            ps.setNull(3, Types.TIME);
        } else {
            ps.setTime(3, Time.valueOf(cita.getHora()));
        }

        ps.setString(4, cita.getTipoServicio());
        ps.setString(5, cita.getDescripcion());
        ps.setString(6, cita.getEstado());
    }

    private CitaTecnica mapear(ResultSet rs) throws SQLException {
        CitaTecnica cita = new CitaTecnica();

        cita.setId(rs.getInt("id"));
        cita.setClienteId(rs.getInt("cliente_id"));
        cita.setClienteNombre(rs.getString("cliente_nombre"));

        Date fecha = rs.getDate("fecha");
        if (fecha != null) {
            cita.setFecha(fecha.toLocalDate());
        }

        Time hora = rs.getTime("hora");
        if (hora != null) {
            cita.setHora(hora.toLocalTime());
        }

        cita.setTipoServicio(rs.getString("tipo_servicio"));
        cita.setDescripcion(rs.getString("descripcion"));
        cita.setEstado(rs.getString("estado"));

        return cita;
    }
}
