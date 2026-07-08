package com.minerva.domain.entities.result;

import java.util.Optional;

import com.minerva.domain.constants.DomainError;

// Nota: chat gpt recomienda usar la palbra fuilure en vez de fail, porque dice que failure es sutatntivo
public class Result<D> {
    private final boolean success;
    private final String message;
    private final D data;
    private final DomainError domainError;

    private Result(boolean success, String message, D data, DomainError domainError) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.domainError = domainError;
    }

    public static <D> Result<D> success(D data) {
        return new Result<>(true, "", data, null);
    }

    public static <D> Result<D> success(D data, String message) {
        return new Result<>(true, message, data, null);
    }

    public static <D> Result<D> fail(String message) {
        return new Result<>(false, message, null, null);
    }

    public static <D> Result<D> fail(DomainError domainError) {
        return new Result<>(false, domainError.name(), null, domainError);
    }

    public boolean isSuccess() { return success; }
    public boolean isFail() {return !success;}
    public String getMessage() { return message; }
    public D getData() { return data; }
    public Optional<DomainError> getDomainError() { return Optional.ofNullable(domainError); }

}
