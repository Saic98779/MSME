package com.metaverse.msme.repository;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSummary;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSummaryCounts;
import com.metaverse.msme.msme_unit_details.service.MsmeUnitSummaryResponse;
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

    @Query("""
            SELECT u
            FROM MsmeUnitDetails u
            WHERE LOWER(TRIM(FUNCTION('regexp_replace', COALESCE(u.district, ''), '\\s*\\([^)]*\\)', '', 'g'))) =
                  LOWER(TRIM(FUNCTION('regexp_replace', :district, '\\s*\\([^)]*\\)', '', 'g')))
              AND (:mandal IS NULL OR LOWER(TRIM(FUNCTION('regexp_replace', COALESCE(u.mandal, ''), '\\s*\\([^)]*\\)', '', 'g'))) =
                  LOWER(TRIM(FUNCTION('regexp_replace', :mandal, '\\s*\\([^)]*\\)', '', 'g'))))
              AND (:village IS NULL OR LOWER(COALESCE(u.village, '')) = LOWER(:village))
            """)
    List<MsmeUnitDetails> findDuplicateCandidates(@Param("district") String district, @Param("mandal") String mandal, @Param("village") String village);

    @Query(value = """
            SELECT
                COUNT(*) AS target,
                COALESCE(SUM(CASE WHEN is_completed = true THEN 1 ELSE 0 END), 0) AS completedMsmes,
                COALESCE(SUM(CASE WHEN is_completed = false THEN 1 ELSE 0 END), 0) AS pendingMsmes,
                COALESCE(SUM(CASE WHEN is_completed IS NULL THEN 1 ELSE 0 END), 0) AS yetToBegin,
                COALESCE(SUM(CASE WHEN is_new_unit = true THEN 1 ELSE 0 END), 0) AS newMsmes,
                COALESCE(SUM(CASE WHEN is_duplicate = true THEN 1 ELSE 0 END), 0) AS duplicatedMsmes
            FROM msme_unit_details
            WHERE LOWER(CAST(extracteddistrict AS TEXT)) = LOWER(:district)
            """, nativeQuery = true)
    MsmeUnitSummaryCounts fetchDistrictSummary(@Param("district") String district);

    @Query(value = """
            SELECT
                COUNT(*) AS target,
                COALESCE(SUM(CASE WHEN is_completed = true THEN 1 ELSE 0 END), 0) AS completedMsmes,
                COALESCE(SUM(CASE WHEN is_completed = false THEN 1 ELSE 0 END), 0) AS pendingMsmes,
                COALESCE(SUM(CASE WHEN is_completed IS NULL THEN 1 ELSE 0 END), 0) AS yetToBegin,
                COALESCE(SUM(CASE WHEN is_new_unit = true THEN 1 ELSE 0 END), 0) AS newMsmes,
                COALESCE(SUM(CASE WHEN is_duplicate = true THEN 1 ELSE 0 END), 0) AS duplicatedMsmes
            FROM msme_unit_details
            WHERE LOWER(CAST(extracteddistrict AS TEXT)) = LOWER(:district)
              AND LOWER(CAST(extractedmandal AS TEXT)) = LOWER(:mandal)
            """, nativeQuery = true)
    MsmeUnitSummaryCounts fetchDistrictMandalSummary(@Param("district") String district, @Param("mandal") String mandal);

    @Query(value = """
            SELECT
                COUNT(*) AS target,
                COALESCE(SUM(CASE WHEN is_completed = true THEN 1 ELSE 0 END), 0) AS completedMsmes,
                COALESCE(SUM(CASE WHEN is_completed = false THEN 1 ELSE 0 END), 0) AS pendingMsmes,
                COALESCE(SUM(CASE WHEN is_completed IS NULL THEN 1 ELSE 0 END), 0) AS yetToBegin,
                COALESCE(SUM(CASE WHEN is_new_unit = true THEN 1 ELSE 0 END), 0) AS newMsmes,
                COALESCE(SUM(CASE WHEN is_duplicate = true THEN 1 ELSE 0 END), 0) AS duplicatedMsmes
            FROM msme_unit_details
            WHERE LOWER(CAST(extracteddistrict AS TEXT)) = LOWER(:district)
              AND LOWER(CAST(extractedmandal AS TEXT)) = LOWER(:mandal)
              AND LOWER(CAST(extractedvillage AS TEXT)) = LOWER(:village)
            """, nativeQuery = true)
    MsmeUnitSummaryCounts fetchDistrictMandalVillageSummary(@Param("district") String district, @Param("mandal") String mandal, @Param("village") String village);

    @Query("SELECT u FROM MsmeUnitDetails u WHERE u.msmeUnitId > :after ORDER BY u.msmeUnitId ASC")
    List<MsmeUnitDetails> findNextChunk(@Param("after") Integer after, Pageable pageable);

    default Page<MsmeUnitSummary> findAllSummaries(Specification<MsmeUnitDetails> spec, Pageable pageable) {
        return findBy(spec, query -> query.as(MsmeUnitSummary.class).page(pageable));
    }


    @Query(value = """
            SELECT
                extractedvillage,
                COUNT(*) AS target,
                COALESCE(SUM(CASE WHEN is_completed = true THEN 1 ELSE 0 END), 0) AS completedMsmes,
                COALESCE(SUM(CASE WHEN is_completed = false THEN 1 ELSE 0 END), 0) AS pendingMsmes,
                COALESCE(SUM(CASE WHEN is_completed IS NULL THEN 1 ELSE 0 END), 0) AS yetToBegin,
                COALESCE(SUM(CASE WHEN is_new_unit = true THEN 1 ELSE 0 END), 0) AS newMsmes,
                COALESCE(SUM(CASE WHEN is_duplicate = true THEN 1 ELSE 0 END), 0) AS duplicatedMsmes
            FROM msme_unit_details
            WHERE LOWER(CAST(extracteddistrict AS TEXT)) = LOWER(:district)
              AND LOWER(CAST(extractedmandal AS TEXT)) = LOWER(:mandal)
            GROUP BY extractedvillage
            ORDER BY extractedvillage
            """, nativeQuery = true)
   List<MsmeUnitSummaryCounts> fetchVillageSummary(@Param("district") String district, @Param("mandal") String mandal);


}
