package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MsmeUnitDetailsService {
    private final MsmeUnitDetailsRepository unitDetailsRepository;

    @Transactional
    public MsmeUnitDetailsDto updateMsmeUnitDetails(Long msmeUnitId, MsmeUnitDetailsDto request) {
        MsmeUnitDetails existing;
        if (msmeUnitId == null) {
            existing = new MsmeUnitDetails();
        } else {
            existing = unitDetailsRepository.findById(msmeUnitId)
                    .orElseThrow(() -> new RuntimeException("MSME Unit Details not found with slno: " + msmeUnitId));
        }

        MsmeUnitDetailsMapper.mapUpdateMsmeUnitDetails(request, existing);

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(unitDetailsRepository.save(existing));
    }

    public MsmeUnitDetailsDto getMsmeUnitById(Long msmeUnitId) {

        MsmeUnitDetails existing = unitDetailsRepository.findById(msmeUnitId)
                .orElseThrow(() -> new RuntimeException(
                        "MSME Unit Details not found with slno: " + msmeUnitId));

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(existing);
    }

    @Transactional(readOnly = true)
    public Page<MsmeUnitSearchResponse> searchMsmeUnits(MsmeUnitSearchRequest request, int page, int size) {
        MsmeUnitSearchRequest safeRequest = request != null ? request : new MsmeUnitSearchRequest();

        int resolvedPage = Math.max(page, 0);
        int resolvedSize = size <= 0 ? 10 : Math.min(size, 100);

        Specification<MsmeUnitDetails> specification = MsmeUnitSpecification.searchByCriteria(safeRequest);

        Pageable pageable = PageRequest.of(resolvedPage, resolvedSize);

        Page<MsmeUnitSummary> resultPage = unitDetailsRepository.findAllSummaries(specification, pageable);

        if (resultPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return resultPage.map(this::mapToSearchResponse);
    }

    private MsmeUnitSearchResponse mapToSearchResponse(MsmeUnitSummary unit) {
        return MsmeUnitSearchResponse.builder()
                .unitName(unit.getUnitName())
                .ownerName(unit.getUnitHolderOrOwnerName())
                .udyamNumber(unit.getUdyamRegistrationNo())
                .mobileNumber(unit.getMobileNo())
                .district(unit.getDistrict())
                .mandal(unit.getMandal())
                .village(unit.getVillage())
                .build();
    }

}
