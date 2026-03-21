package com.metaverse.msme.repository;

import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.msme_unit_details.service.MsmeDuplicateCriteriaCheck;
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

    @Query(value = """
            SELECT
                CAST(msme_unit_id AS INTEGER) AS msmeUnitId,
                unitname AS unitName,
                unitholderorownername AS ownerName,
                extracteddistrict AS extractedistrict,
                extractedmandal AS extractemandal,
                extractedvillage AS extractevillage
            FROM msme_unit_details
            WHERE LOWER(TRIM(BOTH FROM regexp_replace(COALESCE(CAST(extracteddistrict AS TEXT), ''), '\\s*\\([^)]*\\)', '', 'g'))) =
                  :normalizedDistrict
              AND (
                    similarity(LOWER(COALESCE(CAST(unitname AS TEXT), '')), :normalizedUnitName) >= 0.20
                 OR similarity(LOWER(COALESCE(CAST(unitholderorownername AS TEXT), '')), :normalizedOwnerName) >= 0.20
                 OR (:normalizedMandal IS NOT NULL AND similarity(LOWER(COALESCE(CAST(extractedmandal AS TEXT), '')), :normalizedMandal) >= 0.20)
                 OR (:normalizedVillage IS NOT NULL AND similarity(LOWER(COALESCE(CAST(extractedvillage AS TEXT), '')), :normalizedVillage) >= 0.20)
              )
            ORDER BY GREATEST(
                    similarity(LOWER(COALESCE(CAST(unitname AS TEXT), '')), :normalizedUnitName),
                    similarity(LOWER(COALESCE(CAST(unitholderorownername AS TEXT), '')), :normalizedOwnerName),
                    CASE WHEN :normalizedMandal IS NULL THEN 0 ELSE similarity(LOWER(COALESCE(CAST(extractedmandal AS TEXT), '')), :normalizedMandal) END,
                    CASE WHEN :normalizedVillage IS NULL THEN 0 ELSE similarity(LOWER(COALESCE(CAST(extractedvillage AS TEXT), '')), :normalizedVillage) END
            ) DESC
            LIMIT 250
            """, nativeQuery = true)
    List<MsmeDuplicateCriteriaCheck> findDuplicateCandidates(@Param("normalizedDistrict") String normalizedDistrict,
                                                             @Param("normalizedUnitName") String normalizedUnitName,
                                                             @Param("normalizedOwnerName") String normalizedOwnerName,
                                                             @Param("normalizedMandal") String normalizedMandal,
                                                             @Param("normalizedVillage") String normalizedVillage);

    @Query(value = """
            SELECT
                CAST(msme_unit_id AS INTEGER) AS msmeUnitId,
                unitname AS unitName,
                unitholderorownername AS ownerName,
                extracteddistrict AS extractedistrict,
                extractedmandal AS extractemandal,
                extractedvillage AS extractevillage
            FROM msme_unit_details
            WHERE LOWER(TRIM(BOTH FROM regexp_replace(COALESCE(CAST(extracteddistrict AS TEXT), ''), '\\s*\\([^)]*\\)', '', 'g'))) =
                  :normalizedDistrict
            """, nativeQuery = true)
    List<MsmeDuplicateCriteriaCheck> findDuplicateCandidatesByDistrict(@Param("normalizedDistrict") String normalizedDistrict);

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
