package com.minerva.domain.entities.shared;

import com.minerva.domain.exceptions.DomainException;

public class DomainExceptionResult<D> extends Result<D> {
    private final DomainException domainException;

    private DomainExceptionResult(boolean success, DomainException exception, D data) {
        super(success, exception.getMessage(), data);
        this.domainException = exception;
    }

    public static <D> Result<D> success(D data, DomainException exception) {
        return new DomainExceptionResult<>(true, exception, data);
    }

    public static <D> Result<D> fail(DomainException exception) {
        return new DomainExceptionResult<>(false, exception, null);
    }

    public DomainException getDomainException() {
        return domainException;
    }
}
