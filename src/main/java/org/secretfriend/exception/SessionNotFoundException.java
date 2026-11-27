package org.secretfriend.exception;

import org.secretfriend.exception.base.NotFoundException;

public class SessionNotFoundException extends NotFoundException {
    public SessionNotFoundException(String sessionId) {
        super(sessionId, "Session not found");
    }
}
