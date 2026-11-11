package org.secretfriend.exception;

import org.secretfriend.exception.base.NotFoundException;

public class SessionOrPersonNotFound extends NotFoundException {

    public SessionOrPersonNotFound() {
        super("Session or person's name not found");
    }
}
