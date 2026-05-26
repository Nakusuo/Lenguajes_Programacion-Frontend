package com.minerva.domain.exceptions;

public class MinimumAmountException extends DomainException {

    public MinimumAmountException(String minAmount, Throwable cause) {
        super("El monto no puede ser menor que " + minAmount + ".", cause);
    }

    public MinimumAmountException(String minAmount) {
        super("El monto no puede ser menor que " + minAmount + ".");
    }
}