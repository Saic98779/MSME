package com.metaverse.msme.msme_unit_details.service;

public interface MsmeUnitSummaryCounts {

    String getExtractedvillage();

    String getExtractedmandal();

    Long getTarget();

    Long getCompletedMsmes();

    Long getPendingMsmes();

    Long getNewMsmes();

    Long getDuplicatedMsmes();

    Long getYetToBegin();
}
