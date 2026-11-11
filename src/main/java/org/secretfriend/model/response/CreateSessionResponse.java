package org.secretfriend.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateSessionResponse(

        @JsonProperty("session_id") String sessionId
) {
}
