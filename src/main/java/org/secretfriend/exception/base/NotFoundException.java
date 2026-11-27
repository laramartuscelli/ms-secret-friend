package org.secretfriend.exception.base;

import lombok.Getter;

public abstract class NotFoundException extends RuntimeException {

    @Getter
    private final String sessionId;

    protected NotFoundException(String message) {
        super(message);
        this.sessionId = null;
    }

    protected NotFoundException(String sessionId, String message) {
        super(message);
        this.sessionId = sessionId;
    }
}
