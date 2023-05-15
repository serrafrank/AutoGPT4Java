package org.erlik.autogpt4java.exceptions;

public class LocalizationException extends NotFoundException {

    public LocalizationException(String message) {
        super(message);
    }

    public LocalizationException(String message, Exception exception) {
        super(message, exception);
    }
}
