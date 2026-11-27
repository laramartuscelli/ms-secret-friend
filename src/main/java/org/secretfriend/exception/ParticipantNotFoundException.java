package org.secretfriend.exception;

import lombok.Getter;
import org.secretfriend.exception.base.NotFoundException;

public class ParticipantNotFoundException extends NotFoundException {

    @Getter
    private final String participantName;

    public ParticipantNotFoundException(String sessionId, String participantName) {
        super(sessionId, "Participant not found in this session");
        this.participantName = participantName;
    }
}
