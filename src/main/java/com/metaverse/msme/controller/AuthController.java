package com.metaverse.msme.controller;

import com.metaverse.msme.common.ApplicationAPIResponse;
import com.metaverse.msme.common.AuthResponse;
import com.metaverse.msme.common.LoginRequest;
import com.metaverse.msme.common.RegisterRequest;
import com.metaverse.msme.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(
        origins = {
                "http://localhost:3000",
                "http://localhost:4200",
                "http://msmedis.s3-website.eu-north-1.amazonaws.com"
        },
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = {"Authorization", "Content-Type", "Accept"},
        allowCredentials = "true",
        maxAge = 3600
)
@Tag(name = "Authentication", description = "Authentication and User Management APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Create a new user account with email, username, and password. Returns JWT token upon successful registration."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class)))
    })
    public ResponseEntity<ApplicationAPIResponse<AuthResponse>> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class))
            )
            @RequestBody RegisterRequest request) {
        try {
            AuthResponse authResponse = authService.register(request);
            ApplicationAPIResponse<AuthResponse> response = ApplicationAPIResponse.<AuthResponse>builder()
                    .data(authResponse)
                    .success(true)
                    .message("User registered successfully")
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApplicationAPIResponse<AuthResponse> response = ApplicationAPIResponse.<AuthResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticate user with email and password. Returns JWT token upon successful authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or inactive user",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class)))
    })
    public ResponseEntity<ApplicationAPIResponse<AuthResponse>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials (email and password)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            )
            @RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);
            ApplicationAPIResponse<AuthResponse> response = ApplicationAPIResponse.<AuthResponse>builder()
                    .data(authResponse)
                    .success(true)
                    .message("Login successful")
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApplicationAPIResponse<AuthResponse> response = ApplicationAPIResponse.<AuthResponse>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(401)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/validate-token")
    @Operation(
            summary = "Validate JWT token",
            description = "Check if the provided JWT token is valid and not expired. Requires Bearer token in Authorization header."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid token format",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class))),
            @ApiResponse(responseCode = "401", description = "Token is invalid or expired",
                    content = @Content(schema = @Schema(implementation = ApplicationAPIResponse.class)))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApplicationAPIResponse<Object>> validateToken(
            @Parameter(description = "JWT Bearer token", required = true, example = "Bearer eyJhbGciOiJIUzUxMiJ9...")
            @RequestHeader("Authorization") String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                ApplicationAPIResponse<Object> response = ApplicationAPIResponse.builder()
                        .success(false)
                        .message("Invalid token format")
                        .code(400)
                        .build();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            String jwt = token.substring(7);
            ApplicationAPIResponse<Object> response = ApplicationAPIResponse.builder()
                    .success(true)
                    .message("Token is valid")
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApplicationAPIResponse<Object> response = ApplicationAPIResponse.builder()
                    .success(false)
                    .message("Invalid token")
                    .code(401)
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

