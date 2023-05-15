package org.erlik.autogpt4java.exceptions;

public class GenericException extends RuntimeException {

    public GenericException() {
        super();
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Exception exception) {
        super(message, exception);
    }

    public GenericException(Throwable cause) {
        super(cause);
    }

}
