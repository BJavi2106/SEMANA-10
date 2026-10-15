
package com.tuempresa.sistemacitasgestion.util;

import com.tuempresa.sistemacitasgestion.model.CitaTecnica;

import java.util.Set;

public final class ValidadorCita {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "Pendiente",
            "En proceso",
            "Finalizada",
            "Cancelada"
    );

    private ValidadorCita() {
    }

    /**
     * Valida los datos de una cita técnica.
     *
     * @param cita cita que se va a validar
     * @return mensaje del primer error encontrado o null si es válida
     */
    public static String validar(CitaTecnica cita) {
        if (cita == null) {
            return "La cita técnica es obligatoria.";
        }

        if (cita.getClienteId() <= 0) {
            return "Debe seleccionar un cliente válido.";
        }

        if (cita.getFecha() == null) {
            return "Debe indicar la fecha de la cita.";
        }

        if (cita.getHora() == null) {
            return "Debe indicar la hora de la cita.";
        }

        if (vacio(cita.getTipoServicio())) {
            return "Debe indicar el tipo de servicio.";
        }

        if (cita.getTipoServicio().trim().length() > 80) {
            return "El tipo de servicio no puede superar 80 caracteres.";
        }

        if (vacio(cita.getDescripcion())) {
            return "Debe indicar la descripción de la cita.";
        }

        if (cita.getDescripcion().trim().length() > 255) {
            return "La descripción no puede superar 255 caracteres.";
        }

        if (vacio(cita.getEstado())) {
            return "Debe indicar el estado de la cita.";
        }

        if (!ESTADOS_VALIDOS.contains(cita.getEstado())) {
            return "El estado debe ser Pendiente, En proceso, "
                    + "Finalizada o Cancelada.";
        }

        return null;
    }

    private static boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
