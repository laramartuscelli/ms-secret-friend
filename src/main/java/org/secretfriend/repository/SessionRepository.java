package org.secretfriend.repository;

import org.secretfriend.model.document.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    Optional<Session> findByIdAndParticipantsName(String id, String personName);
}
