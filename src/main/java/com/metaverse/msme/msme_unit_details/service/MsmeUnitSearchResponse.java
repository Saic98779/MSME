package com.metaverse.msme.msme_unit_details.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "MSME Unit search response with essential details")
public class MsmeUnitSearchResponse {

    @Schema(description = "Name of the MSME unit", example = "Sameer Industries")
    private String unitName;

    @Schema(description = "Name of the unit owner/holder", example = "John Doe")
    private String ownerName;

    @Schema(description = "Mobile number", example = "+91 9876543210")
    private String mobileNumber;

    @Schema(description = "District name", example = "Sangareddy")
    private String district;

    @Schema(description = "Mandal name", example = "Patancheru")
    private String mandal;

    @Schema(description = "Village name", example = "Bollaram")
    private String village;
}

