package com.minerva.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando un valor numérico o monetario viola el límite
 * mínimo establecido por las reglas de negocio del dominio.
 */
public class MinimumAmountException extends InvalidDomainArgumentException {

    /**
     * Construye la excepción especificando el monto mínimo requerido y la causa raíz del error.
     *
     * @param message La representación en cadena del valor mínimo permitido (ej. "0").
     * @param cause     La excepción o causa original que provocó este fallo.
     */
    public MinimumAmountException(String message, Throwable cause) {
        super(message, cause, ErrorType.MINIMUM_AMOUNT_VIOLATION);
    }

    /**
     * Construye la excepción especificando el monto mínimo requerido.
     *
     * @param message La representación en cadena del valor mínimo permitido (ej. "0").
     */
    public MinimumAmountException(String message) {
        super(message, ErrorType.MINIMUM_AMOUNT_VIOLATION);
    }
}
