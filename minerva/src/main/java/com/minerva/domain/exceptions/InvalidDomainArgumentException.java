package com.minerva.domain.exceptions;

/**
 * Excepción de dominio base que indica que los datos proporcionados por el usuario
 * o un sistema externo son inválidos para las reglas del negocio.
 */
public class InvalidDomainArgumentException extends DomainException {

    /**
     * Construye la excepción utilizando el tipo de error predeterminado para argumentos inválidos.
     *
     * @param message Mensaje humano descriptivo del error.
     */
    public InvalidDomainArgumentException(String message) {
        super(message, ErrorType.INVALID_ARGUMENT);
    }

    /**
     * Construye la excepción utilizando el tipo de error predeterminado y la causa raíz.
     *
     * @param message Mensaje humano descriptivo del error.
     * @param cause   La excepción original que provocó este fallo.
     */
    public InvalidDomainArgumentException(String message, Throwable cause) {
        super(message, cause, ErrorType.INVALID_ARGUMENT);
    }

    /**
     * Constructor protegido que permite a las clases hijas (como NullValueException)
     * especificar su propio tipo de error más detallado.
     *
     * @param message   Mensaje humano descriptivo del error.
     * @param errorType El tipo de error específico del dominio.
     */
    protected InvalidDomainArgumentException(String message, ErrorType errorType) {
        super(message, errorType);
    }

    /**
     * Constructor protegido que permite a las clases hijas especificar su propio
     * tipo de error detallado junto con la causa raíz.
     *
     * @param message   Mensaje humano descriptivo del error.
     * @param cause     La excepción original que provocó este fallo.
     * @param errorType El tipo de error específico del dominio.
     */
    protected InvalidDomainArgumentException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause, errorType);
    }
}
