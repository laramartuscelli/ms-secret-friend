package org.secretfriend.exception.base;

public abstract class InternalServerErrorException extends RuntimeException {
    protected InternalServerErrorException(String message) {
        super(message);
    }
}
