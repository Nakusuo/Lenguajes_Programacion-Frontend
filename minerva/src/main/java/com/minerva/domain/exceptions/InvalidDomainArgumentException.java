package com.minerva.domain.exceptions;

/**
 * Excepción de dominio base que indica que los datos proporcionados por el usuario
 * o un sistema externo son inválidos para las reglas del negocio.
 */
public class InvalidDomainArgumentException extends DomainException  {
    public InvalidDomainArgumentException(String message, ErrorType errorType) {
        super(message, errorType);
    }

    public InvalidDomainArgumentException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause, errorType);
    }
}
