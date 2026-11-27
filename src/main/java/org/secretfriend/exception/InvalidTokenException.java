package org.secretfriend.exception;

import org.secretfriend.exception.base.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException() {
        super("Invalid or expired token. Please, sign in again");
    }
}
