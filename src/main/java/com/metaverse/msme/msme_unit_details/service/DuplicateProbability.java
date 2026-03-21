package com.metaverse.msme.msme_unit_details.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateProbability {

    private Float probabilityPercentage;
    private MsmeDuplicateCriteriaResponse unitDetails;
}
