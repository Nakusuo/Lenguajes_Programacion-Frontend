package com.minerva.domain.exceptions;

public class UnauthorizedActionException extends DomainException {

    public UnauthorizedActionException(String message) {
        super(message);
    }
}
