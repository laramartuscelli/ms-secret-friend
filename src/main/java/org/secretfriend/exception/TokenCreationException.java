package org.secretfriend.exception;

import org.secretfriend.exception.base.InternalServerErrorException;

public class TokenCreationException extends InternalServerErrorException {

    public TokenCreationException(String user, String message) {
        super("Could not create token for user: " + user + "| Error: " + message);
    }
}
