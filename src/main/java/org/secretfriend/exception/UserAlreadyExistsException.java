package org.secretfriend.exception;

import org.secretfriend.exception.base.UnprocessableContentException;

public class UserAlreadyExistsException extends UnprocessableContentException {

    public UserAlreadyExistsException() {
        super("There's already an user with this email");
    }
}
