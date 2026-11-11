package org.secretfriend.exception.base;

public abstract class UnprocessableContentException extends RuntimeException {
    protected UnprocessableContentException(String message) {
        super(message);
    }
}
