package org.secretfriend.model.dto;

import org.secretfriend.model.request.SignupRequest;
import org.secretfriend.util.PasswordUtils;

public record SignupDto(

        String name,
        String email,
        String password
) {

    public static SignupDto from(SignupRequest request) {

        String hashedPassword = PasswordUtils.hashPassword(request.password());
        return new SignupDto(
                request.name().strip(),
                request.email().strip(),
                hashedPassword
        );
    }
}
