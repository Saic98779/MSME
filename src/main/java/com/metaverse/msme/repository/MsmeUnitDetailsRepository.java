package com.metaverse.msme.repository;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MsmeUnitDetailsRepository extends JpaRepository<MsmeUnitDetails, Long>, JpaSpecificationExecutor<MsmeUnitDetails> {
    List<MsmeUnitDetails> findByVillageIgnoreCaseAndMandalIgnoreCase(String village, String mandal);

    @Query("SELECT u FROM MsmeUnitDetails u WHERE u.msmeUnitId > :after ORDER BY u.msmeUnitId ASC")
    List<MsmeUnitDetails> findNextChunk(@Param("after") Long after, Pageable pageable);

    default Page<MsmeUnitSummary> findAllSummaries(Specification<MsmeUnitDetails> spec, Pageable pageable) {
        return findBy(spec, query -> query.as(MsmeUnitSummary.class).page(pageable));
    }
}
