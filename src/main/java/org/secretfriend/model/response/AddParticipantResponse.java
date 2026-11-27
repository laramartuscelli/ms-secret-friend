package org.secretfriend.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.secretfriend.model.Participant;
import org.secretfriend.model.document.Session;

import java.util.List;

public record AddParticipantResponse(

        @JsonProperty("session_id") String sessionId,
        List<PersonResponse> participants
) {

    public static AddParticipantResponse from(Session session) {

        return new AddParticipantResponse(
                session.getId(),
                PersonResponse.from(session.getParticipants())
        );
    }

    public record PersonResponse(
            String name
    ) {

        public static List<PersonResponse> from(List<Participant> participants) {

            return participants.stream()
                    .map(p -> new PersonResponse(p.getName()))
                    .toList();
        }
    }
}
