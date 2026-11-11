package org.secretfriend.exception;

import org.secretfriend.exception.base.UnprocessableContentException;

public class ParticipantExistsWithNameException extends UnprocessableContentException {

    public ParticipantExistsWithNameException() {
        super("There's already a participant with this name");
    }
}
