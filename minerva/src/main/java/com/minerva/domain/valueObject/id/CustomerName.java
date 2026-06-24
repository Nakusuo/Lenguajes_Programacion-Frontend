package com.minerva.domain.valueObject.id;

import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.valueObject.Name;

public class CustomerName extends Name implements CustomerId {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public CustomerName(String value) throws DomainException {
        super(value, MIN_LENGTH, MAX_LENGTH);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public String value() {
        return value;
    }
}
