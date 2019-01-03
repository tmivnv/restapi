package net.uglevodov.restapi.exceptions;

public class NotUpdatableException extends RuntimeException {
    public NotUpdatableException(String message) {
        super(message);
    }
}