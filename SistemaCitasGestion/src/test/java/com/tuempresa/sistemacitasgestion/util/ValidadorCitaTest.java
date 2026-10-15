
package com.tuempresa.sistemacitasgestion.util;

import com.tuempresa.sistemacitasgestion.model.CitaTecnica;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValidadorCitaTest {

    @Test
    void rechazaCitaNula() {
        assertEquals(
                "La cita técnica es obligatoria.",
                ValidadorCita.validar(null)
        );
    }

    @Test
    void rechazaClienteInvalido() {
        CitaTecnica cita = crearCitaValida();
        cita.setClienteId(0);

        assertEquals(
                "Debe seleccionar un cliente válido.",
                ValidadorCita.validar(cita)
        );
    }

    @Test
    void rechazaFechaNula() {
        CitaTecnica cita = crearCitaValida();
        cita.setFecha(null);

        assertEquals(
                "Debe indicar la fecha de la cita.",
                ValidadorCita.validar(cita)
        );
    }

    @Test
    void rechazaHoraNula() {
        CitaTecnica cita = crearCitaValida();
        cita.setHora(null);

        assertEquals(
                "Debe indicar la hora de la cita.",
                ValidadorCita.validar(cita)
        );
    }

    @Test
    void rechazaTipoServicioVacio() {
        CitaTecnica cita = crearCitaValida();
        cita.setTipoServicio(" ");

        assertEquals(
                "Debe indicar el tipo de servicio.",
                ValidadorCita.validar(cita)
        );
    }

    @Test
    void rechazaDescripcionVacia() {
        CitaTecnica cita = crearCitaValida();
        cita.setDescripcion("");

        assertEquals(
                "Debe indicar la descripción de la cita.",
                ValidadorCita.validar(cita)
        );
    }

    @Test
    void rechazaEstadoVacio() {
        CitaTecnica cita = crearCitaValida();
        cita.setEstado("");

        assertEquals(
                "Debe indicar el estado de la cita.",
                ValidadorCita.validar(cita)
        );
    }

    @Test
    void rechazaEstadoNoPermitido() {
        CitaTecnica cita = crearCitaValida();
        cita.setEstado("En espera");

        assertNotNull(ValidadorCita.validar(cita));
    }

    @Test
    void aceptaCitaValida() {
        CitaTecnica cita = crearCitaValida();

        assertNull(ValidadorCita.validar(cita));
    }

    private CitaTecnica crearCitaValida() {
        return new CitaTecnica(
                0,
                1,
                "Cliente de prueba",
                LocalDate.of(2026, 10, 20),
                LocalTime.of(10, 30),
                "Mantenimiento",
                "Revisión general",
                "Pendiente"
        );
    }
}
