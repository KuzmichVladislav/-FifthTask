package com.company.task5.exception;

public class LogisticException extends Exception {
    public LogisticException() {
        super();
    }

    public LogisticException(String message) {
        super(message);
    }

    public LogisticException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogisticException(Throwable cause) {
        super(cause);
    }
}
