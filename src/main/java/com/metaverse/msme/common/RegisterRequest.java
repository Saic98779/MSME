package com.metaverse.msme.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "User registration request payload")
public class RegisterRequest {

    @Schema(description = "User email address (must be unique)", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "User password (will be encrypted)", example = "SecurePass123!", required = true)
    private String password;

    @Schema(description = "Username (must be unique)", example = "johndoe", required = true)
    private String username;

    @Schema(description = "First name", example = "John")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    private String lastName;

    @Schema(description = "Mobile number", example = "+1234567890")
    private String mobileNo;

    @Schema(description = "User role", example = "USER", defaultValue = "USER", allowableValues = {"USER", "ADMIN", "MANAGER"})
    private String userRole;
}

