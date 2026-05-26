package com.minerva.domain.exceptions;

public class MoneyOperationException extends DomainException {

    public MoneyOperationException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}