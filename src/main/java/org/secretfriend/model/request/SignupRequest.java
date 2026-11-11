package org.secretfriend.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupRequest(

        @NotBlank(message = "Please, inform the user's name")
        String name,

        @NotBlank(message = "Please, inform the user's email")
        @Email(message = "Please, inform a valid email")
        String email,

        @NotBlank(message = "Please, inform the user's password")
        String password
) {
}
