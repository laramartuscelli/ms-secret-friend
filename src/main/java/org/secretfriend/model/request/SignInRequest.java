package org.secretfriend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignInRequest(

        @NotBlank(message = "Please, inform the email")
        @Email(message = "Please, inform a valid email")
        String email,

        @NotBlank(message = "Please, inform the password")
        String password
) {
}
