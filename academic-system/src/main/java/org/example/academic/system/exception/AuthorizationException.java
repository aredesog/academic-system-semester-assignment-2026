package org.example.academic.system.exception;

public class AuthorizationException extends SecurityException {
    public AuthorizationException(String message) {
        super(message);
    }
}
