package com.minerva.domain.valueObject.id;

import com.minerva.domain.interfaces.Id;

public class AllId implements Id<String> {
    private final String value = "ALL";

    @Override
    public String value() {
        return value;
    }

    @Override
    public String asString() {
        return value;
    }
    
}
