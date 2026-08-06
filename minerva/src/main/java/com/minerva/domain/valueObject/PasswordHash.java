package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.NullValueException;

public class PasswordHash extends ValueObject<String> {
    public PasswordHash(String value) throws NullValueException {
        super(value);
    }
}
