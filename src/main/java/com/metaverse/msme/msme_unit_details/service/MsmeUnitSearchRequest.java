package com.metaverse.msme.msme_unit_details.service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Search criteria for MSME units")
public class MsmeUnitSearchRequest {

    @Schema(description = "List of districts to filter by", example = "[\"Sangareddy\", \"Hyderabad\"]")
    private List<String> districts;

    @Schema(description = "List of mandals to filter by", example = "[\"Patancheru\", \"Bollaram\"]")
    private List<String> mandals;

    @Schema(description = "List of villages to filter by", example = "[\"Bollaram\", \"IDA Jeedimetla\"]")
    private List<String> villages;

    @Schema(description = "Unit name to search for (partial match supported)", example = "Sameer")
    private String unitName;

    @Schema(description = "Mobile number to search for", example = "9876543210")
    private String mobileNumber;

    @Schema(description = "Page number (0-based). Used if query params are not provided.", example = "0")
    private Integer page;

    @Schema(description = "Page size. Used if query params are not provided.", example = "10")
    private Integer size;
}
