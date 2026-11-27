package org.secretfriend.repository;

import org.secretfriend.model.document.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
