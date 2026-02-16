package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MsmeUnitDetailsService {
    private final MsmeUnitDetailsRepository unitDetailsRepository;

    @Transactional
    public MsmeUnitDetailsDto updateMsmeUnitDetails(Long msmeUnitId, MsmeUnitDetailsDto request) {

        MsmeUnitDetails existing = unitDetailsRepository.findById(msmeUnitId)
                .orElseThrow(() -> new RuntimeException("MSME Unit Details not found with slno: " + msmeUnitId));

        MsmeUnitDetailsMapper.mapUpdateMsmeUnitDetails(request, existing);

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(unitDetailsRepository.save(existing));
    }

    public MsmeUnitDetailsDto getMsmeUnitById(Long msmeUnitId) {

        MsmeUnitDetails existing = unitDetailsRepository.findById(msmeUnitId)
                .orElseThrow(() -> new RuntimeException(
                        "MSME Unit Details not found with slno: " + msmeUnitId));

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(existing);
    }

    public Page<MsmeUnitSearchResponse> searchMsmeUnits(
            MsmeUnitSearchRequest request,
            int page,
            int size) {

        Specification<MsmeUnitDetails> specification =
                MsmeUnitSpecification.searchByCriteria(request);

        Pageable pageable = PageRequest.of(page, size);

        Page<MsmeUnitDetails> resultPage =
                unitDetailsRepository.findAll(specification, pageable);

        return resultPage.map(this::mapToSearchResponse);
    }

    private MsmeUnitSearchResponse mapToSearchResponse(MsmeUnitDetails unit) {
        return MsmeUnitSearchResponse.builder()
                .unitName(unit.getUnitName())
                .ownerName(unit.getUnitHolderOrOwnerName())
                .mobileNumber(unit.getOfficeContact())
                .district(unit.getDistrict())
                .mandal(unit.getMandal())
                .village(unit.getVillage())
                .build();
    }

}
