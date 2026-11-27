package org.secretfriend.service;

import lombok.RequiredArgsConstructor;
import org.secretfriend.exception.ParticipantExistsWithNameException;
import org.secretfriend.exception.ParticipantNotFoundException;
import org.secretfriend.exception.SessionNotFoundException;
import org.secretfriend.model.Participant;
import org.secretfriend.model.document.Session;
import org.secretfriend.model.response.AddParticipantResponse;
import org.secretfriend.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final SessionRepository sessionRepository;

    @Transactional
    public AddParticipantResponse addParticipantToSession(String sessionId, String participantName) {

        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        String cleanParticipantName = participantName.strip();
        if (existingSession.hasParticipant(cleanParticipantName)) {
            throw new ParticipantExistsWithNameException();
        }

        existingSession.addParticipant(new Participant(cleanParticipantName));
        Session updatedSession = sessionRepository.save(existingSession);
        return AddParticipantResponse.from(updatedSession);
    }

    @Transactional
    public void removeParticipantFromSession(String sessionId, String participantName) {

        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow((() -> new SessionNotFoundException(sessionId)));

        String cleanParticipantName = participantName.strip();
        Participant participant = existingSession.getParticipant(cleanParticipantName)
                .orElseThrow(() -> new ParticipantNotFoundException(sessionId, cleanParticipantName));

        existingSession.removeParticipant(participant);
        sessionRepository.save(existingSession);
    }
}
