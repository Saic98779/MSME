package com.metaverse.msme.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Login request payload")
public class LoginRequest {

    @Schema(description = "User email address", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "User password", example = "password123", required = true)
    private String password;
}

