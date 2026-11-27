package org.secretfriend.config.security;

import org.secretfriend.exception.PasswordTooLongException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder(12);

    @Override
    public String encode(CharSequence rawPassword) {

        if (rawPassword == null) {
            throw new IllegalArgumentException("Null password");
        }

        String passwordAsString = rawPassword.toString();

        if (passwordAsString.length() > 72 || passwordAsString.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new PasswordTooLongException();
        }

        return bcryptEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return bcryptEncoder.matches(rawPassword, encodedPassword);
    }
}
