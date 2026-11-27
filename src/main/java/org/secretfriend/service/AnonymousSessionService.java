package org.secretfriend.service;

import lombok.RequiredArgsConstructor;
import org.secretfriend.exception.ParticipantNotFoundException;
import org.secretfriend.exception.SessionNotFoundException;
import org.secretfriend.exception.SessionNotSortedException;
import org.secretfriend.model.Participant;
import org.secretfriend.model.document.Session;
import org.secretfriend.model.response.CreateSessionResponse;
import org.secretfriend.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnonymousSessionService {

    private final SessionRepository sessionRepository;

    @Transactional
    public CreateSessionResponse createEmptySession() {

        Session emptySession = new Session();
        Session savedSession = sessionRepository.save(emptySession);
        return new CreateSessionResponse(savedSession.getId());
    }

    @Transactional
    public void sortSession(String sessionId) {

        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        existingSession.sort();
        sessionRepository.save(existingSession);
    }

    @Transactional(readOnly = true)
    public String getSecretFriend(String sessionId, String participantName) {

        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        if (!existingSession.isSorted()) {
            throw new SessionNotSortedException(sessionId);
        }

        String cleanParticipantName = participantName.strip().toUpperCase();
        Participant participant = existingSession.getParticipant(cleanParticipantName)
                .orElseThrow(() -> new ParticipantNotFoundException(sessionId, cleanParticipantName));

        return participant.getSecretFriend();
    }
}
