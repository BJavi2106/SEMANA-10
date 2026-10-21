
package com.tuempresa.sistemacitasgestion.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class TareasEnSegundoPlano {

    private static final ExecutorService EJECUTOR =
            Executors.newFixedThreadPool(3);

    private TareasEnSegundoPlano() {
        // Evita crear instancias de esta clase.
    }

    public static void ejecutar(Runnable tarea) {
        EJECUTOR.submit(tarea);
    }

    public static void cerrar() {
        EJECUTOR.shutdown();
    }
}
