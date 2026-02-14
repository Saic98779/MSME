package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
