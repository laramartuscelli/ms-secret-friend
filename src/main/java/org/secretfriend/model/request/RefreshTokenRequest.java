package org.secretfriend.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(

        @NotBlank(message = "Please, inform the refresh token")
        @JsonProperty("refresh_token") String refreshToken
) {
}
