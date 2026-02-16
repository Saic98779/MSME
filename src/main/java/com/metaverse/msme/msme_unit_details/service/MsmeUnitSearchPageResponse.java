package com.metaverse.msme.msme_unit_details.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsmeUnitSearchPageResponse {
    private List<MsmeUnitSearchResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}

