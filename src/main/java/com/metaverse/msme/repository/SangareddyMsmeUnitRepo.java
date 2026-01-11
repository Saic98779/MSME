package com.metaverse.msme.repository;


import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.model.SangareddyMsmeUnitEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SangareddyMsmeUnitRepo extends JpaRepository<SangareddyMsmeUnitEntity, Integer> {

    List<MsmeUnitDetails> findByVillageIgnoreCaseAndMandalIgnoreCase(String village, String mandal);

    @Query("SELECT u FROM SangareddyMsmeUnitEntity u WHERE u.slno > :after ORDER BY u.slno ASC")
    List<SangareddyMsmeUnitEntity> findNextChunk(@Param("after") Integer after, Pageable pageable);




}
