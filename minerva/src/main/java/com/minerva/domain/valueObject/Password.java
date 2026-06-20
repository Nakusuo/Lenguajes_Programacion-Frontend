package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public final class Password extends ValueObject<String> {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 100;

    public Password(String value) throws DomainException {
        super(value);
        
        if (value.isBlank()) throw new DomainException("El PASSWORD no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El PASSWORD debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH)throw new DomainException("El PASSWORD no puede exceder los " + MAX_LENGTH + " caracteres.");
    }

}
