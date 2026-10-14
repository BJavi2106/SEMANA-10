package com.tuempresa.sistemacitasgestion.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public final class ConexionBD {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConexionBD.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Falta src/main/resources/db.properties");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer db.properties", e);
        }
    }

    private ConexionBD() {}

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }

    // Statement para SQL fijo sin datos del usuario
    public static boolean probar() {
        try (Connection cn = ConexionBD.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1")) {
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
}