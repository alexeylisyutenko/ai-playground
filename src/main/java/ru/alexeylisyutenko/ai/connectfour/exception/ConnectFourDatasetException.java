package ru.alexeylisyutenko.ai.connectfour.exception;

public class ConnectFourDatasetException extends RuntimeException {
    public ConnectFourDatasetException(String message) {
        super(message);
    }

    public ConnectFourDatasetException(String message, Throwable cause) {
        super(message, cause);
    }
}
