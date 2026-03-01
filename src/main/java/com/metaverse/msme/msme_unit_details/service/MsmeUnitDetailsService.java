package com.metaverse.msme.msme_unit_details.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.model.MsmeUnitDetailsHistory;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import com.metaverse.msme.repository.MsmeUnitDetailsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MsmeUnitDetailsService {
    private final MsmeUnitDetailsRepository unitDetailsRepository;
    private final MsmeUnitDetailsHistoryRepository historyRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public MsmeUnitDetailsDto updateMsmeUnitDetails(Long msmeUnitId, MsmeUnitDetailsDto request, String userId) {
        MsmeUnitDetails existing;
        if (msmeUnitId == null) {
            existing = new MsmeUnitDetails();
            // Set userId for new records
            existing.setUserId(userId);
        } else {
            existing = unitDetailsRepository.findById(msmeUnitId)
                    .orElseThrow(() -> new RuntimeException("MSME Unit Details not found with slno: " + msmeUnitId));
            // Update userId for existing records if not already set
            if (existing.getUserId() == null) {
                existing.setUserId(userId);
            }
        }

        MsmeUnitDetailsMapper.mapUpdateMsmeUnitDetails(request, existing);

        MsmeUnitDetails saved = unitDetailsRepository.save(existing);

        // Save to history table
        saveToHistory(saved, userId);

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(saved);
    }

    public MsmeUnitDetailsDto getMsmeUnitById(Long msmeUnitId) {

        MsmeUnitDetails existing = unitDetailsRepository.findById(msmeUnitId)
                .orElseThrow(() -> new RuntimeException(
                        "MSME Unit Details not found with slno: " + msmeUnitId));

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(existing);
    }

    @Transactional(readOnly = true)
    public Page<MsmeUnitSearchResponse> searchMsmeUnits(MsmeUnitSearchRequest request) {
        MsmeUnitSearchRequest safeRequest = request != null ? request : new MsmeUnitSearchRequest();

        Specification<MsmeUnitDetails> specification = MsmeUnitSpecification.searchByCriteria(safeRequest);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<MsmeUnitSummary> resultPage = unitDetailsRepository.findAllSummaries(specification, pageable);

        if (resultPage.isEmpty()) {
            return Page.empty(pageable);
        }

        return resultPage.map(this::mapToSearchResponse);
    }

    private MsmeUnitSearchResponse mapToSearchResponse(MsmeUnitSummary unit) {
        return MsmeUnitSearchResponse.builder()
                .msmeUnitId(unit.getMsmeUnitId())
                .unitName(unit.getUnitName())
                .ownerName(unit.getUnitHolderOrOwnerName())
                .udyamNumber(unit.getUdyamRegistrationNo())
                .mobileNumber(unit.getMobileNo())
                .district(unit.getDistrict())
                .mandal(unit.getMandal())
                .village(unit.getVillage())
                .stageNumber(unit.getStageNumber())
                .build();
    }


    private void saveToHistory(MsmeUnitDetails details, String updatedBy) {
        com.fasterxml.jackson.databind.DeserializationFeature feature = com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
        MsmeUnitDetailsHistory history = objectMapper.copy()
                .disable(feature)
                .convertValue(details, MsmeUnitDetailsHistory.class);

        history.setHistoryId(null);

        history.setUpdatedBy(updatedBy);
        history.setUpdatedAt(LocalDateTime.now());
        history.setChangeDescription("MSME Unit Details Updated");

        historyRepository.save(history);
    }

}
