package com.metaverse.msme.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(description = "Authentication response with JWT token and user details")
public class AuthResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyLWlkIiwiZW1haWwiOiJ1c2VyQGV4YW1wbGUuY29tIn0...")
    private String token;

    @Schema(description = "Unique user identifier (UUID)", example = "123e4567-e89b-12d3-a456-426614174000")
    private String userId;

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "Username", example = "johndoe")
    private String username;

    @Schema(description = "User role", example = "USER")
    private String userRole;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Response message", example = "Login successful")
    private String message;
}

