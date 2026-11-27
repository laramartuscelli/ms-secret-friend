package org.secretfriend.exception;

import org.secretfriend.exception.base.UnprocessableContentException;

public class SessionNotSortedException extends UnprocessableContentException {
    public SessionNotSortedException(String sessionId) {
        super(sessionId, "This session isn't sorted yet");
    }
}
