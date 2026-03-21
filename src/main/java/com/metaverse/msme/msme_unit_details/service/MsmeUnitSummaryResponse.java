package com.metaverse.msme.msme_unit_details.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsmeUnitSummaryResponse {

    private String district;
    private String mandal;
    private String village;

    private Long target;
    private Long completedMsmes;
    private Long pendingMsmes;
    private Long newMsmes;
    private Long duplicatedMsmes;
    private Long yetToBegin;
}
