package org.secretfriend.exception.base;

import lombok.Getter;

public abstract class UnprocessableContentException extends RuntimeException {

    @Getter
    private final String sessionId;

    protected UnprocessableContentException(String message) {
        super(message);
        this.sessionId = null;
    }

    protected UnprocessableContentException(String sessionId, String message) {
        super(message);
        this.sessionId = sessionId;
    }
}
