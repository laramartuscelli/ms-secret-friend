package org.secretfriend.exception.base;

public abstract class UnauthorizedException extends RuntimeException {
    protected UnauthorizedException(String message) {
        super(message);
    }
}
