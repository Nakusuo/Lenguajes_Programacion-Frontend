package com.minerva.domain.exceptions;

public class DomainException extends Exception {

    public final ErrorType errorType;

    public DomainException(String message) {
        super(message);
        this.errorType = ErrorType.GENERIC;
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorType = ErrorType.GENERIC;
    }

    protected DomainException(String message, Throwable cause, ErrorType errorType) {
        super(message, cause);
        this.errorType = errorType;
    }

    protected DomainException(String message, ErrorType errorType) {
        super(message);
        this.errorType = errorType;
    }

    public enum ErrorType  {
        GENERIC,
        UNAUTHORIZED_ACTION,
        NULL_VALUE,
        MINIMUM_AMOUNT_VIOLATION,
        INVALID_ARGUMENT
    }

}
