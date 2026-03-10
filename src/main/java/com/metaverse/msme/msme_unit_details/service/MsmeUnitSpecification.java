package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MsmeUnitSpecification {

    public static Specification<MsmeUnitDetails> searchByCriteria(MsmeUnitSearchRequest request) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Mandal filter
            extractFirst(request.getMandals())
                    .ifPresent(mandal -> predicates.add(cb.equal(cb.lower(root.get("mandal")), mandal)));

            // Village filter
            extractFirst(request.getVillages())
                    .ifPresent(village -> predicates.add(cb.equal(cb.lower(root.get("village")), village)));

            // Unit Name
            if (hasValue(request.getUnitName())) {
                predicates.add(cb.like(cb.lower(root.get("unitName")),
                        "%" + request.getUnitName().trim().toLowerCase() + "%"));
            }

            // Mobile Number
            if (hasValue(request.getMobileNumber())) {
                predicates.add(cb.like(root.get("mobileNo"),
                        "%" + request.getMobileNumber().trim() + "%"));
            }

            // Stage Number
            if (request.getStageNumber() != null) {
                predicates.add(request.getStageNumber() < 7
                        ? cb.lessThan(root.get("stageNumber"), 7)
                        : cb.equal(root.get("stageNumber"), 7));
            }

            query.distinct(true);

            return predicates.isEmpty()
                    ? cb.conjunction()
                    : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Optional<String> extractFirst(List<String> list) {
        if (list == null || list.isEmpty()) return Optional.empty();
        String value = list.get(0);
        return (value != null && !value.trim().isEmpty())
                ? Optional.of(value.trim().toLowerCase())
                : Optional.empty();
    }

    private static boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty();
    }
}