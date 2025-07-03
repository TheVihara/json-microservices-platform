package net.unnamed.common.database;

public record Result<T>(boolean success, T data, String message) {
    public static <T> Result<T> success(T data) {
        return new Result<>(true, data, null);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>(false, null, message);
    }
}
