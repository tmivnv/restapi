package net.uglevodov.restapi.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class NotUpdatableException extends RuntimeException {
    public NotUpdatableException(String message) {
        super(message);
    }
}