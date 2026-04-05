package com.metaverse.msme.msme_unit_details.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for duplicate probability evaluation")
public class DuplicateProbabilityRequest {

    @Schema(description = "Unit name", example = "sameer")
    private String unitName;

    @Schema(description = "Owner name", example = "sameer")
    private String ownerName;

    @Schema(description = "District name", example = "warangal")
    private String district;

    @Schema(description = "Mobile number", example = "9876543210")
    private String mobileNo;

    @Schema(description = "Mandal name", example = "hanamkonda")
    private String mandal;

    @Schema(description = "Village name", example = "kazipet")
    private String villages;
}
