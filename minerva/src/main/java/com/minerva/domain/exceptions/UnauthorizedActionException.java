package com.minerva.domain.exceptions;

public class UnauthorizedActionException extends DomainException {

    public UnauthorizedActionException() {
        super("No está autorizado para ejecutar esta acción.");
    }
}
