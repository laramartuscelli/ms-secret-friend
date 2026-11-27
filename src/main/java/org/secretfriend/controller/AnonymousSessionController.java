package org.secretfriend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.secretfriend.model.response.AddParticipantResponse;
import org.secretfriend.model.response.CreateSessionResponse;
import org.secretfriend.service.ParticipantService;
import org.secretfriend.service.AnonymousSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Tag(name = "Session")
@RequestMapping("/anonymous-session")
@RequiredArgsConstructor
@SecurityRequirement()
public class AnonymousSessionController {

    private final AnonymousSessionService anonymousSessionService;
    private final ParticipantService participantService;

    @PostMapping
    @Operation(description = "Create a new Secret Friend session")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<CreateSessionResponse> createSession() {

        CreateSessionResponse response = anonymousSessionService.createEmptySession();
        return ResponseEntity.created(URI.create("/" + response.sessionId())).body(response);
    }

    @PutMapping("/add-person/{session-id}")
    @Operation(description = "Add person to existing session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "422", description = "There's already a participant with this name"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<AddParticipantResponse> addPersonToSession(
            @Parameter(name = "session-id", description = "The unique number that identifies a session")
            @PathVariable("session-id") String sessionId,

            @Parameter(name = "person-name", description = "The name of the participant")
            @NotBlank(message = "Please, inform the person's name")
            @RequestHeader("person-name") String personName) {

        return ResponseEntity.ok(participantService.addParticipantToSession(sessionId, personName));
    }

    @PutMapping("/remove-person/{session-id}")
    @Operation(description = "Remove person from existing session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Removed"),
            @ApiResponse(responseCode = "404", description = "Session or person not found")
    })
    public ResponseEntity<Void> removePersonFromSession(
            @Parameter(name = "session-id", description = "The unique number that identifies a session")
            @PathVariable("session-id") String sessionId,

            @Parameter(name = "person-name", description = "The person's name")
            @NotBlank(message = "Please, inform the person's name")
            @RequestHeader("person-name") String personName) {

        participantService.removeParticipantFromSession(sessionId, personName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/secret-friend/{session-id}")
    @Operation(description = "Gets the secret friend of a participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participant's secret friend"),
            @ApiResponse(responseCode = "404", description = "Participant or Session not found"),
            @ApiResponse(responseCode = "422", description = "This session wasn't sorted yet")
    })
    public ResponseEntity<String> getSecretFriend(
            @Parameter(name = "session-id", description = "The unique number that identifies a session")
            @PathVariable("session-id") String sessionId,

            @Parameter(name = "participant-name", description = "The name of the session's participant")
            @RequestHeader("participant-name") String participantName) {

        return ResponseEntity.ok(anonymousSessionService.getSecretFriend(sessionId, participantName));
    }

    @PutMapping("/sort/{session-id}")
    @Operation(description = "Sort an existing session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sorted"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<Void> sortSession(@PathVariable("session-id") String sessionId) {

        anonymousSessionService.sortSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
