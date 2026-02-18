package com.metaverse.msme.service;

import com.metaverse.msme.address.AddressNormalizer;
import com.metaverse.msme.address.AdminNameParts;
import com.metaverse.msme.extractor.*;
import com.metaverse.msme.model.*;
import com.metaverse.msme.repository.MsmeUnitDetailsRepository;
import jakarta.persistence.EntityManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AddressParseService {

    private final MandalDetector mandalDetector;
    private final VillageDetector villageDetector;
    private final AddressNormalizer addressNormalizer;
    private final MsmeUnitDetailsRepository repository;

    @Autowired
    private  AddressNormalizer normalizer;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private  EntityManager entityManager;
    public AddressParseService(
            MandalDetector mandalDetector,
            VillageDetector villageDetector,
            AddressNormalizer addressNormalizer, MsmeUnitDetailsRepository repository) {

        this.mandalDetector = mandalDetector;
        this.villageDetector = villageDetector;
        this.addressNormalizer = addressNormalizer; // ✅ MUST ASSIGN
        this.repository = repository;
    }

    public AddressParseResult parse(String district, String address) {

        // 1️⃣ Detect mandal normally
        MandalDetectionResult mandalResult = mandalDetector.detectMandal(district, address);

        if(mandalResult.getStatus() == MandalDetectionStatus.MULTIPLE_DISTRICTS){
             return AddressParseResult.fromDistrict(mandalResult);

        }
    /* ----------------------------------------------------------
       CASE: MANDAL NOT FOUND → DISTRICT LEVEL VILLAGE FALLBACK
       ---------------------------------------------------------- */
        if (mandalResult.getStatus() == MandalDetectionStatus.MANDAL_NOT_FOUND) {

            System.out.println("USING detectVillageAcrossDistrict");

            VillageDetectionResult vResult = villageDetector.detectVillageAcrossDistrict(district, address);

            // ---------- SINGLE VILLAGE ----------
            if (vResult.getStatus() == VillageDetectionStatus.SINGLE_VILLAGE) {

                String village = vResult.getVillage();

                Set<String> mandals = villageDetector.findMandalsByVillage(district, village);

                // One mandal → assign
                if (mandals.size() == 1) {
                    String m = mandals.iterator().next();
                    return AddressParseResult.combineResolved(
                            MandalDetectionResult.single(m),
                            m,
                            vResult
                    );
                }

                // Multiple mandals → check raw address
                Optional<String> resolvedFromRaw =
                        resolveMandalFromRawAddress(address, mandals);

                if (resolvedFromRaw.isPresent()) {
                    String m = resolvedFromRaw.get();
                    return AddressParseResult.combineResolved(
                            MandalDetectionResult.single(m),
                            m,
                            vResult
                    );
                }

                // Still ambiguous
                return AddressParseResult.combineResolved(
                        MandalDetectionResult.multipleMandalsFromVillage(mandals),
                        null,
                        vResult
                );
            }

            // ---------- MULTIPLE VILLAGES ----------
            if (vResult.getStatus() == VillageDetectionStatus.MULTIPLE_VILLAGES) {
                return AddressParseResult.combineResolved(
                        MandalDetectionResult.notFound(),
                        null,
                        vResult
                );
            }

            //---------- VILLAGE NOT FOUND ----------
            if (vResult.getStatus() == VillageDetectionStatus.VILLAGE_NOT_FOUND) {

                // 1️⃣ Normalize & merge address
                String normalized = addressNormalizer.normalize(address);
                String merged = normalized.replaceAll("\\s+", "");

                // 2️⃣ Get district hierarchy JSON
                JSONObject root = mandalDetector.getDistrictHierarchy(district);
                if (root == null) {
                    return AddressParseResult.combineResolved(
                            MandalDetectionResult.notFound(),
                            null,
                            VillageDetectionResult.notFound()
                    );
                }

                JSONArray mandalsArr = root.getJSONArray("mandals");

                // 3️⃣ Loop mandals
                for (int i = 0; i < mandalsArr.length(); i++) {

                    JSONObject mandalObj = mandalsArr.getJSONObject(i);
                    String mandalName = mandalObj.getString("mandalName");

                    String mandalKey = addressNormalizer
                            .normalize(mandalName)
                            .replaceAll("\\s+", "");

                    // ✅ Remember mandal match (DO NOT return yet)
                    boolean mandalMatched = merged.contains(mandalKey);

                    // 🔹 Villages loop (CHECK FIRST)
                    JSONArray villagesArr = mandalObj.getJSONArray("villages");

                    for (int j = 0; j < villagesArr.length(); j++) {

                        JSONObject villageObj = villagesArr.getJSONObject(j);
                        String villageName = villageObj.getString("villageName");

                        String villageKey = addressNormalizer
                                .normalize(villageName)
                                .replaceAll("\\s+", "");

                        // 1️⃣ Village name substring
                        if (merged.contains(villageKey)) {
                            return AddressParseResult.combineResolved(
                                    MandalDetectionResult.single(mandalName),
                                    mandalName,
                                    VillageDetectionResult.single(villageName)
                            );
                        }

                        // 2️⃣ Village alias substring
                        if (!villageObj.isNull("aliasName")) {

                            String aliasKey = addressNormalizer
                                    .normalize(villageObj.getString("aliasName"))
                                    .replaceAll("\\s+", "");

                            if (merged.contains(aliasKey)) {
                                return AddressParseResult.combineResolved(
                                        MandalDetectionResult.single(mandalName),
                                        mandalName,
                                        VillageDetectionResult.single(villageName)
                                );
                            }
                        }
                    }

                    // 🔹 Mandal-only fallback (AFTER village loop)
                    if (mandalMatched) {
                        return AddressParseResult.combineResolved(
                                MandalDetectionResult.single(mandalName),
                                mandalName,
                                VillageDetectionResult.notFound()
                        );
                    }
                }
            }


            // ---------- VILLAGE NOT FOUND ----------
            return AddressParseResult.combineResolved(
                    MandalDetectionResult.notFound(),
                    null,
                    VillageDetectionResult.notFound()
            );
        }

    /* ----------------------------------------------------------
       NORMAL FLOW → Mandal already found
       ---------------------------------------------------------- */
        if (mandalResult.getStatus() == MandalDetectionStatus.MULTIPLE_MANDALS) {

            Set<String> mandals = mandalResult.getMatchedMandals();

            if (mandals != null && !mandals.isEmpty()) {
                return resolveMultipleMandalsUsingVillage(
                        district,
                        address,
                        mandals
                );
            }

            return AddressParseResult.fromMandalResult(mandalResult);
        }


        if (mandalResult.getStatus() != MandalDetectionStatus.SINGLE_MANDAL) {
            return AddressParseResult.fromMandalResult(mandalResult);
        }

        // 2️⃣ Use DB mandal name for village detection
        String dbMandal = mandalResult.getMandal();

        VillageDetectionResult villageResult =
                villageDetector.detectVillage(
                        district,
                        dbMandal,
                        address
                );

        // 3️⃣ Resolve mandal display-friendly version
        String displayMandal =
                resolveMandalDisplayName(dbMandal, address);

        return AddressParseResult.combineResolved(
                mandalResult,
                displayMandal,
                villageResult
        );
    }

    // ✅ YOUR DYNAMIC METHOD LIVES HERE
    private String resolveMandalDisplayName(
            String dbMandalName,
            String address) {

        AdminNameParts dbParts = parseAdminName(dbMandalName);
        Set<String> addrWords = new HashSet<>(
                Arrays.asList(
                        addressNormalizer.normalize(address).split(" ")
                )
        );

        for (String q : dbParts.getQualifiers()) {
            if (addrWords.contains(q)) {
                return dbMandalName;
            }
        }

        return capitalize(dbParts.getBaseName());
    }

    private String capitalize(String s) {
        String[] arr = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : arr) {
            sb.append(Character.toUpperCase(w.charAt(0)))
                    .append(w.substring(1))
                    .append(" ");
        }
        return sb.toString().trim();
    }

    public AdminNameParts parseAdminName(String name) {

        String norm = addressNormalizer.normalize(name);

        String[] words = norm.split(" ");

        Set<String> qualifiers = new LinkedHashSet<>();
        List<String> baseParts = new ArrayList<>();

        for (String w : words) {
            // qualifier = word originally inside brackets in DB
            if (name.toLowerCase().contains("(" + w + ")")) {
                qualifiers.add(w);
            } else {
                baseParts.add(w);
            }
        }
        return new AdminNameParts(String.join(" ", baseParts).trim(), qualifiers);
    }

    private Optional<String> resolveMandalFromRawAddress(
            String rawAddress,
            Set<String> candidateMandals) {

        if (candidateMandals == null || candidateMandals.isEmpty()) {
            return Optional.empty();
        }

        for (String mandal : candidateMandals) {
            if (mandalPresentInRaw(rawAddress, mandal)) {
                return Optional.of(mandal);
            }
        }
        return Optional.empty();
    }


    private boolean mandalPresentInRaw(String rawAddress, String mandalName) {

        if (rawAddress == null || mandalName == null) {
            return false;
        }

        List<String> tokens = normalizer.meaningfulTokenSet(rawAddress);

        String mandalPh = phoneticNormalize(normalizer.normalize(mandalName));

        for (String t : tokens) {

            String tokenPh = phoneticNormalize(normalizer.normalize(t));

            // exact phonetic
            if (tokenPh.equals(mandalPh)) {
                return true;
            }

            // fuzzy safety
            if (similarity(tokenPh, mandalPh) >= 0.90) {
                return true;
            }
        }
        return false;
    }

    private String phoneticNormalize(String s) {
        return s.replace("oo", "u")
                .replace("oor", "ur");
    }

    private double similarity(String s1, String s2) {
        int dist = levenshtein(s1, s2);
        int max = Math.max(s1.length(), s2.length());
        return max == 0 ? 1.0 : 1.0 - ((double) dist / max);
    }

    private int levenshtein(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private AddressParseResult resolveMultipleMandalsUsingVillage(
            String district,
            String address,
            Set<String> mandals) {

        // 1️⃣ Detect village across district (NOT mandal-scoped)
        VillageDetectionResult vResult =
                villageDetector.detectVillageAcrossDistrict(district, address);

        if (vResult.getStatus() != VillageDetectionStatus.SINGLE_VILLAGE) {
            return AddressParseResult.fromMandalResult(
                    MandalDetectionResult.multiple(mandals)
            );
        }

        String village = vResult.getVillage();

        // 2️⃣ Find mandals that actually contain this village
        Set<String> mandalsByVillage =
                villageDetector.findMandalsByVillage(district, village);

        // 3️⃣ Intersect with detected mandals
        mandalsByVillage.retainAll(mandals);

        // ✅ Exactly ONE mandal owns this village
        if (mandalsByVillage.size() == 1) {
            String resolvedMandal = mandalsByVillage.iterator().next();

            return AddressParseResult.combineResolved(
                    MandalDetectionResult.single(resolvedMandal),
                    resolvedMandal,
                    VillageDetectionResult.single(village.toLowerCase())
            );
        }

        // ❌ Village belongs to multiple mandals OR none
        return AddressParseResult.fromMandalResult(
                MandalDetectionResult.multiple(mandals)
        );
    }

}