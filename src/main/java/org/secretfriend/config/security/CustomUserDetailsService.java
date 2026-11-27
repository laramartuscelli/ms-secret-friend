package org.secretfriend.config.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.secretfriend.exception.UserNotFoundException;
import org.secretfriend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) throws UserNotFoundException {

        return userRepository.findByEmail(email)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
