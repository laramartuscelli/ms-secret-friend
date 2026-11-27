package org.secretfriend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.secretfriend.model.request.RefreshTokenRequest;
import org.secretfriend.model.request.SignInRequest;
import org.secretfriend.model.response.TokenResponse;
import org.secretfriend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication")
@RequestMapping("/auth")
@RequiredArgsConstructor
@SecurityRequirement()
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(description = "Signs user in and returns access tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns access and refresh tokens"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TokenResponse> authenticateAndGenerateTokens(
            @NotNull(message = "Please, inform request's data")
            @Valid @RequestBody SignInRequest request) {


        return ResponseEntity.ok(authService.authenticateAndGenerateTokens(request.email(), request.password()));
    }

    @PostMapping("/refresh-token")
    @Operation(description = "Refreshes user's token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns refreshed tokens"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<TokenResponse> refreshToken(
            @NotNull(message = "Please, inform request's data")
            @Valid @RequestBody RefreshTokenRequest request) {

        return ResponseEntity.ok(authService.refreshToken(request.refreshToken()));
    }
}
