package org.secretfriend.service;

import lombok.RequiredArgsConstructor;
import org.secretfriend.exception.UserAlreadyExistsException;
import org.secretfriend.model.document.User;
import org.secretfriend.model.dto.SignupDto;
import org.secretfriend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public String createUser(SignupDto dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException();
        }

        User createdUser = userRepository.save(new User(dto));
        return createdUser.getId();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
}
