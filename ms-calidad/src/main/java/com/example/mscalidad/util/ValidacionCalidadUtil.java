package com.example.mscalidad.util;

public class ValidacionCalidadUtil {

    /**
     * Valida que el resultado sea "Aprobado" o "Rechazado" (ignorando mayúsculas/minúsculas).
     *
     * @param resultado el texto a validar
     * @return true si es válido, false en caso contrario
     */
    public static boolean esResultadoValido(String resultado) {
        if (resultado == null) {
            return false;
        }
        return resultado.equalsIgnoreCase("Aprobado") || resultado.equalsIgnoreCase("Rechazado");
    }
}
