package org.secretfriend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.secretfriend.config.security.CustomUserDetails;
import org.secretfriend.exception.InvalidTokenException;
import org.secretfriend.exception.TokenCreationException;
import org.secretfriend.model.document.User;
import org.secretfriend.model.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;

    private final Clock clock;

    private static final String ISSUER = "Secret Friend Application";

    public TokenResponse generateTokens(CustomUserDetails user) {

        return new TokenResponse(
                generateToken(user, TokenType.ACCESS),
                generateToken(user, TokenType.REFRESH)
        );
    }

    @Nullable
    public CustomUserDetails getAuthenticatedUser(String token) {

        if (token == null || token.isBlank()) {
            return null;
        }

        DecodedJWT decodedJWT = JWT
                .require(Algorithm.HMAC512(secret))
                .withIssuer(ISSUER)
                .build()
                .verify(token);

        if (decodedJWT.getSubject() == null) return null;

        User user = new User(
                decodedJWT.getClaim("id").asString(),
                decodedJWT.getSubject());

        return new CustomUserDetails(user);
    }

    public String getUserIdFromRefreshToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return null;
        }

        DecodedJWT decodedToken = decodeAndValidateRefreshToken(refreshToken);

        String userId = decodedToken.getClaim("id").asString();
        if (userId == null) {
            throw new InvalidTokenException("Invalid token");
        }

        return userId;
    }

    private DecodedJWT decodeAndValidateRefreshToken(String refreshToken) {

        Algorithm algorithm = Algorithm.HMAC512(secret);

        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT
                    .require(algorithm)
                    .build()
                    .verify(refreshToken);

        } catch (JWTVerificationException _) {
            throw new InvalidTokenException("Invalid token");
        }

        String tokenType = decodedJWT.getClaim("type").asString();
        if (tokenType == null || !tokenType.contentEquals(TokenType.REFRESH.getType())) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        return decodedJWT;
    }

    private String generateToken(CustomUserDetails user, TokenType tokenType) {

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withClaim("type", tokenType.getType())
                    .withClaim("id", user.getUserId())
                    .withExpiresAt(tokenType.generateExpiration(clock))
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new TokenCreationException(user.getUsername(), e.getMessage());
        }
    }

    @Getter
    @AllArgsConstructor
    public enum TokenType {
        ACCESS("access", 1, HOURS),
        REFRESH("refresh", 1, DAYS);

        private final String type;
        private final int time;
        private final ChronoUnit unitType;

        public Instant generateExpiration(Clock clock) {

            return ZonedDateTime.now(clock)
                    .plus(time, unitType)
                    .toInstant();
        }
    }
}
