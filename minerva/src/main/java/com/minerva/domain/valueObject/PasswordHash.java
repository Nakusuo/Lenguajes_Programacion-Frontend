package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class PasswordHash extends ValueObject<String> {
    public PasswordHash(String value) throws DomainException {
        super(value);
    }
}
