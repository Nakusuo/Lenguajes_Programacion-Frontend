package com.minerva.domain.entities.shared;

// Nota: chat gpt recomienda usar la palbra fuilure en vez de fail, porque dice que failure es sutatntivo
public class Result<T> {
    private final boolean success;
    private final String message;
    private final T data;

    private Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(true, "", data);
    }

    public static <T> Result<T> success(T data, String message) {
        return new Result<>(true, message, data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(false, message, null);
    }

    public static <T> Result<T> fail() {
        return new Result<>(false, "", null);
    }

    public boolean isSuccess() { return success; }
    public boolean isFail() {return !success;}
    public String getMessage() { return message; }
    public T getData() { return data; }

}
