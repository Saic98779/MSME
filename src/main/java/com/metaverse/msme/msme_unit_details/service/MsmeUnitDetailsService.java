package com.metaverse.msme.msme_unit_details.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.msme.config.DuplicateProbabilityIndexInitializer;
import com.metaverse.msme.model.MsmeUnitDetails;
import com.metaverse.msme.model.User;
import com.metaverse.msme.model.stage.*;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import com.metaverse.msme.repository.MsmeUnitDetailsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MsmeUnitDetailsService {

    private final MsmeUnitDetailsRepository unitDetailsRepository;
    private final MsmeUnitDetailsHistoryRepository historyRepository;
    private final MsmeHistoryAsyncService historyAsyncService;
    private final ObjectMapper objectMapper;
    private final DuplicateProbabilityIndexInitializer duplicateProbabilityIndexInitializer;

    @Transactional
    public MsmeUnitDetailsDto updateMsmeUnitDetails(Long msmeUnitId, MsmeUnitDetailsDto request, String userId) {
        MsmeUnitDetails existing;
        boolean isNew = msmeUnitId == null;

        if (isNew) {
            existing = new MsmeUnitDetails();
            existing.setIsNewUnit(true);
            existing.setUserId(userId);
        } else {
            existing = unitDetailsRepository.findById(msmeUnitId)
                    .orElseThrow(() -> new RuntimeException("MSME Unit Details not found with msme_unit_id: " + msmeUnitId));
            if (existing.getUserId() == null) {
                existing.setIsNewUnit(false);
                existing.setUserId(userId);
            }
        }

        Integer stageNumber = request.getStageNumber();

        Map<String, Object> oldStageData = isNew
                ? Map.of()
                : objectMapper.convertValue(buildStagePojo(existing, stageNumber), Map.class);

        MsmeUnitDetailsMapper.mapUpdateMsmeUnitDetails(request, existing);
        MsmeUnitDetails saved = unitDetailsRepository.save(existing);

        Map<String, Object> newStageData = objectMapper.convertValue(
                buildStagePojo(saved, stageNumber), Map.class);

        Map<String, Object> changedFields = diffStageData(oldStageData, newStageData);

        if (!changedFields.isEmpty()) {
            historyAsyncService.saveHistory(
                    saved.getMsmeUnitId(),
                    userId,
                    stageNumber,
                    changedFields,
                    resolveChangeDescription(stageNumber)
            );
        }

        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(saved);
    }

    public MsmeUnitDetailsDto getMsmeUnitById(Long msmeUnitId) {
        MsmeUnitDetails existing = unitDetailsRepository.findById(msmeUnitId)
                .orElseThrow(() -> new RuntimeException(
                        "MSME Unit Details not found with msme_unit_id: " + msmeUnitId));
        return MsmeUnitDetailsMapper.toMsmeUnitDetailsDto(existing);
    }

    @Transactional(readOnly = true)
    public Page<MsmeUnitSearchResponse> searchMsmeUnits(MsmeUnitSearchRequest request) {
        MsmeUnitSearchRequest safeRequest = request != null ? request : new MsmeUnitSearchRequest();
        Specification<MsmeUnitDetails> specification = MsmeUnitSpecification.searchByCriteria(safeRequest);

        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 50;

        Pageable pageable = PageRequest.of(page, size);
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
                .build();
    }

    // ── Diff ─────────────────────────────────────────────────────────────────

    private Map<String, Object> diffStageData(Map<String, Object> oldData,
                                              Map<String, Object> newData) {
        Map<String, Object> diff = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : newData.entrySet()) {
            String key    = entry.getKey();
            Object newVal = entry.getValue();
            Object oldVal = oldData.get(key);
            if (!Objects.equals(oldVal, newVal)) {
                diff.put(key, Map.of(
                        "old", oldVal != null ? oldVal : "",
                        "new", newVal != null ? newVal : ""));
            }
        }

        for (Map.Entry<String, Object> entry : oldData.entrySet()) {
            String key = entry.getKey();
            if (!newData.containsKey(key) && entry.getValue() != null) {
                diff.put(key, Map.of("old", entry.getValue(), "new", ""));
            }
        }

        return diff;
    }

    // ── Stage builder ─────────────────────────────────────────────────────────

    private Object buildStagePojo(MsmeUnitDetails d, Integer stageNumber) {
        if (stageNumber == null) {
            throw new IllegalArgumentException("stageNumber must not be null");
        }
        return switch (stageNumber) {
            case 1 -> {
                Stage1UnitData s = new Stage1UnitData();
                s.setUnitName(d.getUnitName());
                s.setDistrict(d.getDistrict());
                s.setUnitAddress(d.getUnitAddress());
                s.setDoorNo(d.getDoorNo());
                s.setLocalityStreet(d.getLocalityStreet());
                s.setMandal(d.getMandal());
                s.setVillage(d.getVillage());
                s.setVillageId(d.getVillageid());
                s.setPinCode(d.getPinCode());
                s.setWard(d.getWard());
                s.setOfficeEmail(d.getOfficeEmail());
                s.setOfficeContact(d.getOfficeContact());
                s.setTotalMaleEmployees(d.getTotalMaleEmployees());
                s.setTotalFemaleEmployees(d.getTotalFemaleEmployees());
                yield s;
            }
            case 2 -> {
                Stage2EntrepreneurData s = new Stage2EntrepreneurData();
                s.setUnitHolderOrOwnerName(d.getUnitHolderOrOwnerName());
                s.setFirstMiddleLastName(d.getFirstMiddleLastName());
                s.setGender(d.getGender());
                s.setDateOfBirth(d.getDateOfBirth());
                s.setCaste(d.getCaste());
                s.setSpecialCategory(d.getSpecialCategory());
                s.setQualification(d.getQualification());
                s.setNationality(d.getNationality());
                s.setMobileNo(d.getMobileNo());
                s.setEmailAddress(d.getEmailAddress());
                s.setPanNo(d.getPanNo());
                s.setAadharNo(d.getAadharNo());
                s.setDin(d.getDin());
                s.setPassportNo(d.getPassportNo());
                s.setDesignation(d.getDesignation());
                s.setPhotograph(d.getPhotograph());
                yield s;
            }
            case 3 -> {
                Stage3CommunicationData s = new Stage3CommunicationData();
                s.setCommunicationAddress(d.getCommunicationAddress());
                s.setCommunicationDoorNo(d.getCommunicationDoorNo());
                s.setCommunicationLocalityStreet(d.getCommunicationLocalityStreet());
                s.setCommNameOfTheBuilding(d.getCommNameOfTheBuilding());
                s.setFloorNo(d.getFloorNo());
                s.setCommLandmark(d.getCommLandmark());
                s.setCommunicationVillage(d.getCommunicationVillage());
                s.setCommunicationMandal(d.getCommunicationMandal());
                s.setCommunicationDistrict(d.getCommunicationDistrict());
                s.setCommunicationPinCode(d.getCommunicationPinCode());
                s.setCommAlternateNo(d.getCommAlternateNo());
                yield s;
            }
            case 4 -> {
                Stage4ElectricityData s = new Stage4ElectricityData();
                s.setTypeOfConnection(d.getTypeOfConnection());
                s.setServiceNo(d.getServiceNo());
                s.setLoadKva(d.getLoadKva());
                s.setCurrentStatus(d.getCurrentStatus());
                yield s;
            }
            case 5 -> {
                Stage5ActivityData s = new Stage5ActivityData();
                s.setUnitName(d.getUnitName());
                s.setEnterpriseType(d.getEnterpriseType());
                s.setMsmeSector(d.getMsmeSector());
                s.setOrganizationType(d.getOrganizationType());
                s.setNatureOfBusiness(d.getNatureOfBusiness());
                s.setNicCode(d.getNicCode());
                s.setProductDescription(d.getProductDescription());
                s.setPrincipalBusinessPlace(d.getPrincipalBusinessPlace());
                s.setDepartmentName(d.getDepartmentName());
                s.setMsmeState(d.getMsmeState());
                s.setMsmeDist(d.getMsmeDist());
                s.setIncorporationDate(d.getIncorporationDate());
                s.setCommenceDate(d.getCommenceDate());
                s.setFirmRegYear(d.getFirmRegYear());
                s.setInstitutionDetails(d.getInstitutionDetails());
                s.setCategory(d.getCategory());
                yield s;
            }
            case 6 -> {
                Stage6RegistrationData s = new Stage6RegistrationData();
                s.setUdyamRegistrationNo(d.getUdyamRegistrationNo());
                s.setUdyamRegistrationDate(d.getUdyamRegistrationDate());
                s.setUdyamAadharRegistrationNo(d.getUdyamAadharRegistrationNo());
                s.setGstRegNo(d.getGstRegNo());
                s.setRegistrationUnder(d.getRegistrationUnder());
                s.setRegistrationNo(d.getRegistrationNo());
                s.setUniqueNo(d.getUniqueNo());
                s.setPurpose(d.getPurpose());
                yield s;
            }
            case 7 -> {
                Stage7FinancialData s = new Stage7FinancialData();
                s.setBankName(d.getBankName());
                s.setBranchAddress(d.getBranchAddress());
                s.setIfscCode(d.getIfscCode());
                s.setUnitCostOrInvestment(d.getUnitCostOrInvestment());
                s.setWorkingCapital(d.getWorkingCapital());
                s.setNetTurnoverRupees(d.getNetTurnoverRupees());
                yield s;
            }
            case 8 -> {
                Stage8OperationalData s = new Stage8OperationalData();
                s.setUnitExists(d.getUnitExists());
                s.setUnitWorking(d.getUnitWorking());
                s.setLatitude(d.getLatitude());
                s.setLongitude(d.getLongitude());
                s.setBankLoanAvailed(d.getBankLoanAvailed());
                s.setBankLoanRequired(d.getBankLoanRequired());
                s.setTypeOfLoan(d.getTypeOfLoan());
                s.setSourceOfLoan(d.getSourceOfLoan());
                s.setLoanAppliedDate(d.getLoanAppliedDate());
                s.setLoanSanctionDate(d.getLoanSanctionDate());
                s.setSubsidyApplicationDate(d.getSubsidyApplicationDate());
                s.setReleaseDateDoc(d.getReleaseDateDoc());
                s.setRemarks(d.getRemarks());
                yield s;
            }
            default -> throw new IllegalArgumentException("Unknown stageNumber: " + stageNumber);
        };
    }

    private String resolveChangeDescription(Integer stageNumber) {
        return switch (stageNumber) {
            case 1 -> "Stage 1: Unit/MSME address updated";
            case 2 -> "Stage 2: Entrepreneur details updated";
            case 3 -> "Stage 3: Communication address updated";
            case 4 -> "Stage 4: Electricity details updated";
            case 5 -> "Stage 5: Business activity details updated";
            case 6 -> "Stage 6: Registration details updated";
            case 7 -> "Stage 7: Financial/bank details updated";
            case 8 -> "Stage 8: Operational & loan details updated";
            default -> "Stage " + stageNumber + ": Details updated";
        };
    }

    public List<MsmeUnitSummaryResponse> summaryOfMsmeData(String district, String mandal){
        String normalizedDistrict = normalizeFilter(district);
        String normalizedMandal = normalizeFilter(mandal);

        if (normalizedDistrict == null || normalizedMandal == null) {
            throw new IllegalArgumentException("district and mandal are required");
        }

        List<MsmeUnitSummaryCounts> countsList = unitDetailsRepository.fetchVillageSummary(
                normalizedDistrict,
                normalizedMandal
        );

        Map<String, MsmeUnitSummaryResponse> mergedVillageSummaries = new LinkedHashMap<>();

        for (MsmeUnitSummaryCounts counts : countsList) {
            String village = normalizeFilter(counts.getExtractedvillage());
            String villageKey = village == null ? "" : normalizeLocationText(village);

            MsmeUnitSummaryResponse response = mergedVillageSummaries.get(villageKey);
            if (response == null) {
                response = new MsmeUnitSummaryResponse();
                response.setDistrict(normalizedDistrict);
                response.setMandal(normalizedMandal);
                response.setVillage(village == null ? "" : village);
                response.setTarget(0L);
                response.setCompletedMsmes(0L);
                response.setPendingMsmes(0L);
                response.setNewMsmes(0L);
                response.setDuplicatedMsmes(0L);
                response.setYetToBegin(0L);
                mergedVillageSummaries.put(villageKey, response);
            } else if (shouldReplaceVillageLabel(response.getVillage(), village)) {
                response.setVillage(village);
            }

            response.setTarget(response.getTarget() + safeLong(counts.getTarget()));
            response.setCompletedMsmes(response.getCompletedMsmes() + safeLong(counts.getCompletedMsmes()));
            response.setPendingMsmes(response.getPendingMsmes() + safeLong(counts.getPendingMsmes()));
            response.setNewMsmes(response.getNewMsmes() + safeLong(counts.getNewMsmes()));
            response.setDuplicatedMsmes(response.getDuplicatedMsmes() + safeLong(counts.getDuplicatedMsmes()));
            response.setYetToBegin(response.getYetToBegin() + safeLong(counts.getYetToBegin()));
        }

        return new ArrayList<>(mergedVillageSummaries.values());
    }

    public Page<MsmeUnitSummaryResponse> summaryOfMsmeData(String district, String mandal, Integer page, Integer size) {
        int safePage = page != null && page >= 0 ? page : 0;
        int safeSize = size != null && size > 0 ? size : 10;


        List<MsmeUnitSummaryResponse> summaries = summaryOfMsmeData(district, mandal);

        int start = Math.min(safePage * safeSize, summaries.size());
        int end = Math.min(start + safeSize, summaries.size());
        Pageable pageable = PageRequest.of(safePage, safeSize);

        return new PageImpl<>(summaries.subList(start, end), pageable, summaries.size());
    }


    public MsmeUnitSummaryResponse summaryOfMsmeData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();

        if (userDetails.getDistrict() == null) {
            throw new IllegalArgumentException("district is required");
        }


        MsmeUnitSummaryCounts counts;
        if (userDetails.getMandal() == null) {
            counts = unitDetailsRepository.fetchDistrictSummary(userDetails.getDistrict());
        } else {
            counts = unitDetailsRepository.fetchDistrictMandalSummary(userDetails.getDistrict(), userDetails.getMandal());
        }

        MsmeUnitSummaryResponse response = new MsmeUnitSummaryResponse();
        response.setDistrict(userDetails.getDistrict());
        response.setMandal(userDetails.getMandal());
         response.setTarget(counts.getTarget());
        response.setCompletedMsmes(counts.getCompletedMsmes());
        response.setPendingMsmes(counts.getPendingMsmes());
        response.setNewMsmes(counts.getNewMsmes());
        response.setDuplicatedMsmes(counts.getDuplicatedMsmes());
        response.setYetToBegin(counts.getYetToBegin());
        response.setUser(userDetails);
        return response;
    }

    private String normalizeFilter(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();
        return trimmedValue.isEmpty() ? null : trimmedValue;
    }

    @Transactional(readOnly = true)
    public List<DuplicateProbability> duplicateProbabilityCheck(String unitName, String ownerName, String district, String mandal, String villages) {
        String normalizedUnitName = requireFilter(unitName, "unitName");
        String normalizedOwnerName = requireFilter(ownerName, "ownerName");
        String normalizedDistrict = requireFilter(district, "district");
        String normalizedMandal = normalizeFilter(mandal);
        String normalizedVillage = normalizeFilter(villages);
        String normalizedUnitNameLookup = normalizeText(normalizedUnitName);
        String normalizedOwnerNameLookup = normalizeText(normalizedOwnerName);
        String normalizedDistrictLookup = normalizeLocationText(normalizedDistrict);
        String normalizedMandalLookup = normalizeLocationText(normalizedMandal);
        String normalizedVillageLookup = normalizeText(normalizedVillage);

        List<MsmeDuplicateCriteriaCheck> candidates;

        if (duplicateProbabilityIndexInitializer.isTrigramEnabled()) {
            candidates = unitDetailsRepository.findDuplicateCandidates(
                    normalizedDistrictLookup,
                    normalizedUnitNameLookup,
                    normalizedOwnerNameLookup,
                    normalizedMandalLookup,
                    normalizedVillageLookup
            );
        } else {
            candidates = unitDetailsRepository.findDuplicateCandidatesByDistrict(normalizedDistrictLookup);
        }

        List<DuplicateProbability> response = new ArrayList<>();

        for (MsmeDuplicateCriteriaCheck candidate : candidates) {
            if (isDistrictScopedFuzzyMatch(candidate, normalizedUnitName, normalizedOwnerName, normalizedMandal, normalizedVillage)) {
                response.add(buildProbability(candidate, calculateDistrictScopedMatchPercentage(
                        candidate,
                        normalizedUnitName,
                        normalizedOwnerName,
                        normalizedMandal,
                        normalizedVillage
                )));
            }
        }

        response.sort(Comparator.comparing(DuplicateProbability::getProbabilityPercentage).reversed());
        return response.size() > 1 ? response : List.of();
    }

    private DuplicateProbability buildProbability(MsmeDuplicateCriteriaCheck candidate, float probability) {
        return DuplicateProbability.builder()
                .probabilityPercentage(probability)
                .unitDetails(MsmeDuplicateCriteriaResponse.builder()
                        .msmeUnitId(candidate.getMsmeUnitId())
                        .unitName(candidate.getUnitName())
                        .ownerName(candidate.getOwnerName())
                        .extractedistrict(candidate.getExtractedistrict())
                        .extractemandal(candidate.getExtractemandal())
                        .extractevillage(candidate.getExtractevillage())
                        .mobileNo(candidate.getMobileNo())
                        .build())
                .build();
    }

    private float calculateDistrictScopedMatchPercentage(MsmeDuplicateCriteriaCheck candidate,
                                                         String unitName,
                                                         String ownerName,
                                                         String mandal,
                                                         String village) {
        double total = similarityScore(candidate.getUnitName(), unitName)
                + similarityScore(candidate.getOwnerName(), ownerName);
        int weight = 2;

        if (mandal != null) {
            total += locationSimilarityScore(candidate.getExtractemandal(), mandal);
            weight++;
        }

        if (village != null) {
            total += similarityScore(candidate.getExtractevillage(), village);
            weight++;
        }

        return roundPercentage((total / weight) * 100.0d);
    }

    private boolean isDistrictScopedFuzzyMatch(MsmeDuplicateCriteriaCheck candidate,
                                               String unitName,
                                               String ownerName,
                                               String mandal,
                                               String village) {
        if (!isSimilar(candidate.getUnitName(), unitName, 0.80d)
                || !isSimilar(candidate.getOwnerName(), ownerName, 0.80d)) {
            return false;
        }

        if (mandal != null && !isLocationSimilar(candidate.getExtractemandal(), mandal, 0.85d)) {
            return false;
        }

        return village == null || isSimilar(candidate.getExtractevillage(), village, 0.85d);
    }

    private boolean isSimilar(String left, String right, double threshold) {
        if (!canReachThreshold(left, right, threshold, false)) {
            return false;
        }
        return similarityScore(left, right) >= threshold;
    }

    private boolean isLocationSimilar(String left, String right, double threshold) {
        if (!canReachThreshold(left, right, threshold, true)) {
            return false;
        }
        return locationSimilarityScore(left, right) >= threshold;
    }

    private boolean canReachThreshold(String left, String right, double threshold, boolean location) {
        String normalizedLeft = location ? normalizeLocationText(left) : normalizeText(left);
        String normalizedRight = location ? normalizeLocationText(right) : normalizeText(right);

        if (normalizedLeft == null || normalizedRight == null) {
            return false;
        }

        int maxLength = Math.max(normalizedLeft.length(), normalizedRight.length());
        if (maxLength == 0) {
            return true;
        }

        int minLength = Math.min(normalizedLeft.length(), normalizedRight.length());
        return ((double) minLength / maxLength) >= threshold;
    }

    private double similarityScore(String left, String right) {
        String normalizedLeft = normalizeText(left);
        String normalizedRight = normalizeText(right);

        if (normalizedLeft == null || normalizedRight == null) {
            return 0.0d;
        }

        return similarity(normalizedLeft, normalizedRight);
    }

    private double locationSimilarityScore(String left, String right) {
        String normalizedLeft = normalizeLocationText(left);
        String normalizedRight = normalizeLocationText(right);

        if (normalizedLeft == null || normalizedRight == null) {
            return 0.0d;
        }

        return similarity(normalizedLeft, normalizedRight);
    }

    private double similarity(String left, String right) {
        if (left.equals(right)) {
            return 1.0d;
        }

        int maxLength = Math.max(left.length(), right.length());
        if (maxLength == 0) {
            return 1.0d;
        }

        int distance = levenshteinDistance(left, right);
        return 1.0d - ((double) distance / maxLength);
    }

    private int levenshteinDistance(String left, String right) {
        int[] previous = new int[right.length() + 1];
        int[] current = new int[right.length() + 1];

        for (int j = 0; j <= right.length(); j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= left.length(); i++) {
            current[0] = i;
            for (int j = 1; j <= right.length(); j++) {
                int substitutionCost = left.charAt(i - 1) == right.charAt(j - 1) ? 0 : 1;
                current[j] = Math.min(
                        Math.min(current[j - 1] + 1, previous[j] + 1),
                        previous[j - 1] + substitutionCost
                );
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[right.length()];
    }

    private float roundPercentage(double percentage) {
        return (float) (Math.round(percentage * 100.0d) / 100.0d);
    }

    private String requireFilter(String value, String fieldName) {
        String normalized = normalizeFilter(value);
        if (normalized == null) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return normalized;
    }

    private String normalizeText(String value) {
        String normalized = normalizeFilter(value);
        if (normalized == null) {
            return null;
        }

        String ascii = Normalizer.normalize(normalized, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return ascii.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String normalizeLocationText(String value) {
        String normalized = normalizeFilter(value);
        if (normalized == null) {
            return null;
        }

        String withoutParentheses = normalized.replaceAll("\\s*\\([^)]*\\)", " ");
        return normalizeText(withoutParentheses);
    }

    private boolean shouldReplaceVillageLabel(String existingVillage, String candidateVillage) {
        String normalizedCandidate = normalizeFilter(candidateVillage);
        if (normalizedCandidate == null) {
            return false;
        }

        String normalizedExisting = normalizeFilter(existingVillage);
        if (normalizedExisting == null) {
            return true;
        }

        boolean existingAllUpper = normalizedExisting.equals(normalizedExisting.toUpperCase());
        boolean candidateAllUpper = normalizedCandidate.equals(normalizedCandidate.toUpperCase());
        if (existingAllUpper != candidateAllUpper) {
            return existingAllUpper;
        }

        return normalizedCandidate.length() > normalizedExisting.length();
    }

    private long safeLong(Long value) {
        return value == null ? 0L : value;
    }

    public int markMsmeDuplicate(List<Long> msmeUnitId){
      int updatedCount = 0;
        if (msmeUnitId != null && !msmeUnitId.isEmpty()) {
            updatedCount = unitDetailsRepository.markMsmeDuplicate(new ArrayList<>(msmeUnitId));
        }
        return updatedCount;
    }
}
