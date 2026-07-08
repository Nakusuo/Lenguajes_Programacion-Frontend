package com.minerva.domain.entities.result;

import com.minerva.domain.exceptions.DomainException;

public class DomainExceptionResult<D, E extends DomainException> extends Result<D> {
    private final E domainException;

    private DomainExceptionResult(boolean success, E exception, D data) {
        super(success, exception.getMessage(), data);
        this.domainException = exception;
    }

    public static <D, E extends DomainException> Result<D> success(D data, E exception) {
        return new DomainExceptionResult<>(true, exception, data);
    }

    public static <D, E extends DomainException> Result<D> fail(E exception) {
        return new DomainExceptionResult<>(false, exception, null);
    }

    public E getDomainException() {
        return domainException;
    }
}
