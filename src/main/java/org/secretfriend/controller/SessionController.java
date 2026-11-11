package org.secretfriend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.secretfriend.model.response.AddPersonResponse;
import org.secretfriend.model.response.CreateSessionResponse;
import org.secretfriend.service.PersonService;
import org.secretfriend.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Tag(name = "Session")
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final PersonService personService;

    @PostMapping
    @Operation(description = "Create a new Secret Friend session")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<CreateSessionResponse> createSession() {

        CreateSessionResponse response = sessionService.createEmptySession();
        return ResponseEntity.created(URI.create("/" + response.sessionId())).body(response);
    }

    @PutMapping("/add-person/{session-id}")
    @Operation(description = "Add person to existing session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "422", description = "There's already a participant with this name"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<AddPersonResponse> addPersonToSession(
            @PathVariable("session-id") String sessionId,

            @Parameter(name = "person-name", description = "The name of the participant")
            @NotBlank(message = "Please, inform the person's name")
            @RequestHeader("person-name") String personName) {

        return ResponseEntity.ok(personService.addPersonToSession(sessionId, personName));
    }

    @PutMapping("/remove-person/{session-id}")
    @Operation(description = "Remove person from existing session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Removed"),
            @ApiResponse(responseCode = "404", description = "Session or person not found")
    })
    public ResponseEntity<Void> removePersonFromSession(
            @PathVariable("session-id") String sessionId,

            @Parameter(name = "person-name", description = "The person's name")
            @NotBlank(message = "Please, inform the person's name")
            @RequestHeader("person-name") String personName) {

        personService.removePersonFromSession(sessionId, personName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/sort/{session-id}")
    @Operation(description = "Sort an existing session")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sorted"),
        @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<Void> sortSession(@PathVariable("session-id") String sessionId) {

        sessionService.sortSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
