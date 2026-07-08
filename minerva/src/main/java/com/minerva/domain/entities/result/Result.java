package com.minerva.domain.entities.result;

import java.util.Optional;

import com.minerva.domain.exceptions.DomainException;

// Nota: chat gpt recomienda usar la palbra fuilure en vez de fail, porque dice que failure es sutatntivo
public class Result<D> {
    private final boolean success;
    private final String message;
    private final D data;
    private final DomainException exception;

    private Result(boolean success, String message, D data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.exception = null;
    }

    private Result(DomainException exception) {
        this.success = false;
        this.message = exception.getMessage();
        this.data = null;
        this.exception = exception;
    }

    public static <D> Result<D> success(D data) {
        return new Result<>(true, "", data);
    }

    public static <D> Result<D> success(D data, String message) {
        return new Result<>(true, message, data);
    }

    public static <D> Result<D> fail(String message) {
        return new Result<>(false, message, null);
    }

    public static <D, E extends DomainException> Result<D> fail(E exception) {
        return new Result<>(exception);
    }

    public boolean isSuccess() { return success; }
    public boolean isFail() {return !success;}
    public String getMessage() { return message; }
    public D getData() { return data; }
    public Optional<DomainException> getException() { return Optional.ofNullable(exception); }

}
