package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.model.MsmeUnitDetailsHistory;
import com.metaverse.msme.repository.MsmeUnitDetailsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MsmeHistoryAsyncService {

    private final MsmeUnitDetailsHistoryRepository historyRepository;

    @Async
    @Transactional
    public void saveHistory(Long msmeUnitId, String userId,
                            Integer stageNumber,
                            Map<String, Object> changedFields,
                            String changeDescription) {
        try {
            MsmeUnitDetailsHistory history = new MsmeUnitDetailsHistory();
            history.setMsmeUnitId(msmeUnitId);
            history.setStageNumber(stageNumber);
            history.setUserId(userId);
            history.setStageData(changedFields);
            history.setUpdatedBy(userId);
            history.setUpdatedAt(LocalDateTime.now());
            history.setChangeDescription(changeDescription);
            historyRepository.save(history);
            log.info("History saved async for msmeUnitId={} stage={}", msmeUnitId, stageNumber);
        } catch (Exception e) {
            log.error("Failed to save history async for msmeUnitId={} stage={}: {}",
                    msmeUnitId, stageNumber, e.getMessage(), e);
        }
    }
}