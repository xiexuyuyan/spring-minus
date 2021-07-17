package com.yuyan.wx.login.data;

public class Result<T> {
    private Result() {}

    public final static class Success<T> extends Result<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    @SuppressWarnings("rawtypes")
    public static final class Error extends Result {
        private final Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }
}
