package com.minerva.domain.exceptions;

/**
 * Excepción de dominio lanzada cuando se intenta inicializar o procesar un elemento con un valor nulo
 * que es mandatorio para las reglas del negocio.
 */
public final class NullValueException extends InvalidDomainArgumentException {

    /**
     * Construye la excepción utilizando un mensaje descriptivo predeterminado.
     */
    public NullValueException(String message) {
        super(message, ErrorType.NULL_VALUE);
    }

    public NullValueException(String message, Throwable e) {
        super(message, e, ErrorType.NULL_VALUE);
    }
}
