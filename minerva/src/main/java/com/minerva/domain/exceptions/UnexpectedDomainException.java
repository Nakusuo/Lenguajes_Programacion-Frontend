package com.minerva.domain.exceptions;

public class UnexpectedDomainException extends RuntimeException {

    public UnexpectedDomainException(String message, Throwable cause) {
        super(message, cause);
    }
    public UnexpectedDomainException(String message) {
        super(message);
    }

}