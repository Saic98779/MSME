package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MsmeUnitSpecification {

    public static Specification<MsmeUnitDetails> searchByCriteria(MsmeUnitSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // District filter - multiple selection (case-insensitive, trimmed)
            if (request.getDistricts() != null && !request.getDistricts().isEmpty()) {
                List<String> districts = request.getDistricts().stream()
                        .filter(value -> value != null && !value.trim().isEmpty())
                        .map(value -> value.trim().toLowerCase())
                        .toList();
                if (!districts.isEmpty()) {
                    predicates.add(criteriaBuilder.lower(root.get("district")).in(districts));
                }
            }

            // Mandal filter - multiple selection (case-insensitive, trimmed)
            if (request.getMandals() != null && !request.getMandals().isEmpty()) {
                List<String> mandals = request.getMandals().stream()
                        .filter(value -> value != null && !value.trim().isEmpty())
                        .map(value -> value.trim().toLowerCase())
                        .toList();
                if (!mandals.isEmpty()) {
                    predicates.add(criteriaBuilder.lower(root.get("mandal")).in(mandals));
                }
            }

            // Village filter - multiple selection (case-insensitive, trimmed)
            if (request.getVillages() != null && !request.getVillages().isEmpty()) {
                List<String> villages = request.getVillages().stream()
                        .filter(value -> value != null && !value.trim().isEmpty())
                        .map(value -> value.trim().toLowerCase())
                        .toList();
                if (!villages.isEmpty()) {
                    predicates.add(criteriaBuilder.lower(root.get("village")).in(villages));
                }
            }

            // Unit name filter - partial match (case-insensitive)
            if (request.getUnitName() != null && !request.getUnitName().trim().isEmpty()) {
                String unitName = request.getUnitName().trim().toLowerCase();
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("unitName")),
                        "%" + unitName + "%"
                ));
            }

            // Mobile number filter - exact match or partial match
            if (request.getMobileNumber() != null && !request.getMobileNumber().trim().isEmpty()) {
                String mobile = request.getMobileNumber().trim();
                predicates.add(criteriaBuilder.like(
                        root.get("officeContact"),
                        "%" + mobile + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
