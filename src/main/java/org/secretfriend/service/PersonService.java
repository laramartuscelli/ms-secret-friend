package org.secretfriend.service;

import lombok.RequiredArgsConstructor;
import org.secretfriend.exception.ParticipantExistsWithNameException;
import org.secretfriend.exception.SessionNotFoundException;
import org.secretfriend.exception.SessionOrPersonNotFound;
import org.secretfriend.model.Person;
import org.secretfriend.model.document.Session;
import org.secretfriend.model.response.AddPersonResponse;
import org.secretfriend.repository.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final SessionRepository sessionRepository;

    @Transactional
    public AddPersonResponse addPersonToSession(String sessionId, String personName) {

        Session existingSession = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        personName = personName.strip();
        if (existingSession.hasParticipant(personName)) {
            throw new ParticipantExistsWithNameException();
        }

        existingSession.addParticipant(new Person(personName));
        Session updatedSession = sessionRepository.save(existingSession);
        return AddPersonResponse.from(updatedSession);
    }

    @Transactional
    public void removePersonFromSession(String sessionId, String personName) {

        personName = personName.strip();
        Session existingSession = sessionRepository.findByIdAndParticipantsName(sessionId, personName)
                .orElseThrow(SessionOrPersonNotFound::new);

        existingSession.removeParticipant(personName);
        sessionRepository.save(existingSession);
    }
}
