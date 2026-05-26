package com.minerva.domain.exceptions;

public class DomainException extends Exception {

    public DomainException(String mensaje) {
        super(mensaje);
    }

    public DomainException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
