package org.erlik.autogpt4java.exceptions;

public class NotFoundException extends GenericException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Class<?> clazz, Object value) {
        super(clazz.getSimpleName() + " not found with value " + value.toString());
    }

    public NotFoundException(String message, Exception exception) {
        super(message, exception);
    }
}
