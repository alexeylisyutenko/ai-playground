package ru.alexeylisyutenko.ai.connectfour.exception;

public class ConnectFourDatasetException extends ConnectFourException {
    public ConnectFourDatasetException(String message) {
        super(message);
    }

    public ConnectFourDatasetException(String message, Throwable cause) {
        super(message, cause);
    }
}
