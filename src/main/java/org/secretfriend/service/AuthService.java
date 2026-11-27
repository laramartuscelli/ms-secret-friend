package org.secretfriend.service;

import lombok.RequiredArgsConstructor;
import org.secretfriend.config.security.CustomUserDetails;
import org.secretfriend.exception.InvalidTokenException;
import org.secretfriend.model.document.User;
import org.secretfriend.model.response.TokenResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;

    public TokenResponse authenticateAndGenerateTokens(String email, String password) {

        CustomUserDetails authenticatedUser = authenticate(email, password);
        return tokenService.generateTokens(authenticatedUser);
    }

    public TokenResponse refreshToken(String refreshToken) {

        String userId = tokenService.getUserIdFromRefreshToken(refreshToken);
        User user = userService.findById(userId).orElseThrow(() -> new InvalidTokenException("User not found"));

        return tokenService.generateTokens(new CustomUserDetails(user));
    }

    private CustomUserDetails authenticate(String email, String password) {

        String cleanEmail = email.strip();

        UsernamePasswordAuthenticationToken loginData = UsernamePasswordAuthenticationToken.unauthenticated(cleanEmail, password);
        Authentication authentication = authenticationManager.authenticate(loginData);

        if (authentication.getPrincipal() instanceof CustomUserDetails user) {
            return user;
        }

        throw new BadCredentialsException("Couldn't authenticate user: " + cleanEmail);
    }
}
