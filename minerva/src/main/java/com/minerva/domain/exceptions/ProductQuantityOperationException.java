package com.minerva.domain.exceptions;

public class ProductQuantityOperationException extends DomainException {
    public ProductQuantityOperationException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }
}
