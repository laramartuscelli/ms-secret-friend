package org.secretfriend.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.secretfriend.model.Person;
import org.secretfriend.model.document.Session;

import java.util.List;

public record AddPersonResponse(

        @JsonProperty("session_id") String sessionId,
        List<PersonResponse> participants
) {

    public static AddPersonResponse from(Session session) {

        return new AddPersonResponse(
                session.getId(),
                PersonResponse.from(session.getParticipants())
        );
    }

    public record PersonResponse(
            String name
    ) {

        public static List<PersonResponse> from(List<Person> participants) {

            return participants.stream()
                    .map(p -> new PersonResponse(p.getName()))
                    .toList();
        }
    }
}
