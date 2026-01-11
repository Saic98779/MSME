package com.metaverse.msme.repository;

import com.metaverse.msme.model.SangareddyMsmeUnitEntity;
import com.metaverse.msme.model.TGSPDCLDepartmentUnitDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TGSPDCLDepartmentUnitDetailsRepo extends JpaRepository<TGSPDCLDepartmentUnitDetails, Long> {


    @Query("SELECT u FROM TGSPDCLDepartmentUnitDetails u WHERE u.slno > :after ORDER BY u.slno ASC")
    List<TGSPDCLDepartmentUnitDetails> findNextChunk(@Param("after") Integer after, Pageable pageable);

}
