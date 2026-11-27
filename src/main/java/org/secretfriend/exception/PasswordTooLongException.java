package org.secretfriend.exception;

import org.secretfriend.exception.base.UnprocessableContentException;

public class PasswordTooLongException extends UnprocessableContentException {
    public PasswordTooLongException() {
        super("This password is too long. Please, use up to 72 characters");
    }
}
