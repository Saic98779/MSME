package com.metaverse.msme.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Password update payload")
public class PasswordUpdateRequest {

    @Schema(description = "Current password", example = "OldPassword123", required = true)
    private String currentPassword;

    @Schema(description = "New password", example = "NewPassword123", required = true)
    private String newPassword;

    @Schema(description = "Confirm new password", example = "NewPassword123", required = true)
    private String confirmPassword;
}
