package net.unnamed.common.database;

import java.util.Optional;

public class Result<T> {
    private final boolean success;
    private final T value;
    private final String error;

    private Result(boolean success, T value, String error) {
        this.success = success;
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(true, value, null);
    }

    public static <T> Result<T> failure(String error) {
        return new Result<>(false, null, error);
    }

    public boolean success() {
        return success;
    }

    public T value() {
        return value;
    }

    public Optional<String> error() {
        return Optional.ofNullable(error);
    }
}