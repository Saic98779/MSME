package com.metaverse.msme.repository;

import com.metaverse.msme.model.MsmeUnitDetailsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsmeUnitDetailsHistoryRepository extends JpaRepository<MsmeUnitDetailsHistory, Long> {
    List<MsmeUnitDetailsHistory> findByMsmeUnitIdOrderByUpdatedAtDesc(Long msmeUnitId);

    @Query("SELECT h FROM MsmeUnitDetailsHistory h WHERE h.msmeUnitId = :msmeUnitId ORDER BY h.updatedAt DESC")
    List<MsmeUnitDetailsHistory> findHistoryByMsmeUnitId(@Param("msmeUnitId") Long msmeUnitId);
}

