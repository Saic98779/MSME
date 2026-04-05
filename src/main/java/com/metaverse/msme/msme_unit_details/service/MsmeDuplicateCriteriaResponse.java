package com.metaverse.msme.msme_unit_details.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsmeDuplicateCriteriaResponse {

    private Integer msmeUnitId;
    private String unitName;
    private String ownerName;
    private String extractedistrict;
    private String extractemandal;
    private String extractevillage;
    private String mobileNo;
}
