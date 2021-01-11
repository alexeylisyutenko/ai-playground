package ru.alexeylisyutenko.ai.connectfour.exception;

public class ConnectFourException extends RuntimeException {
    public ConnectFourException(String message) {
        super(message);
    }

    public ConnectFourException(String message, Throwable cause) {
        super(message, cause);
    }
}
