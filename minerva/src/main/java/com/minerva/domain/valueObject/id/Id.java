package com.minerva.domain.valueObject.id;

public interface Id<I> {
    I value();
    String asString();
}
