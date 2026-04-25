package com.metaverse.msme.msme_unit_details.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DistrictMandalSummaryRequest {

    @NotBlank(message = "district is required")
    private String district;

    private String mandal;
}

