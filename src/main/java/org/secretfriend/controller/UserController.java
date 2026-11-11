package org.secretfriend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.secretfriend.model.dto.SignupDto;
import org.secretfriend.model.request.SignupRequest;
import org.secretfriend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@Tag(name = "User")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(description = "Sign up new User")
    @ApiResponse(responseCode = "201", description = "Created")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {

        String userId = userService.createUser(SignupDto.from(request));
        return ResponseEntity.created(URI.create("/" + userId)).build();
    }
}