package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;

public class MsmeUnitDetailsMapper {

    public static void mapUpdateMsmeUnitDetails(MsmeUnitDetailsDto request, MsmeUnitDetails entity) {

        if (request.getStageNumber() == null) {
            throw new IllegalArgumentException("Stage number is required");
        }
        if(request.getStageNumber() > 1) {
            entity.setStageNumber(request.getStageNumber());
        }

        switch (request.getStageNumber()) {
            // ---------------- Stage 1 : Unit/MSME ----------------
            case 1:
                setIfNotNull(request.getDistrict(), entity::setDistrict);
                setIfNotNull(request.getDoorNo(), entity::setDoorNo);
                setIfNotNull(request.getLocalityStreet(), entity::setLocalityStreet);
                setIfNotNull(request.getMandal(), entity::setMandal);
                setIfNotNull(request.getVillage(), entity::setVillage);
                setIfNotNull(request.getPinCode(), entity::setPinCode);

                // Build and set unit address if any component is not null
                if (request.getDoorNo() != null || request.getLocalityStreet() != null ||
                    request.getMandal() != null || request.getVillage() != null ||
                    request.getDistrict() != null || request.getPinCode() != null) {
                    String unitAddress = buildUnitAddress(
                        request.getDoorNo(),
                        request.getLocalityStreet(),
                        request.getMandal(),
                        request.getVillage(),
                        request.getDistrict(),
                        request.getPinCode()
                    );
                    entity.setUnitAddress(unitAddress);
                }
                setIfNotNull(request.getOfficeEmail(), entity::setOfficeEmail);
                setIfNotNull(request.getOfficeContact(), entity::setOfficeContact);
                setIfNotNull(request.getTotalFemaleEmployees(), entity::setTotalFemaleEmployees);
                setIfNotNull(request.getTotalMaleEmployees(), entity::setTotalMaleEmployees);
                break;

            // ---------------- Stage 2 : Entrepreneur ----------------
            case 2:
                setIfNotNull(request.getUnitHolderOrOwnerName(), entity::setUnitHolderOrOwnerName);
                setIfNotNull(request.getCaste(), entity::setCaste);
                setIfNotNull(request.getSpecialCategory(), entity::setSpecialCategory);
                setIfNotNull(request.getGender(), entity::setGender);
                setIfNotNull(request.getDateOfBirth(), entity::setDateOfBirth);
                setIfNotNull(request.getQualification(), entity::setQualification);
                setIfNotNull(request.getPanNo(), entity::setPanNo);
                setIfNotNull(request.getAadharNo(), entity::setAadharNo);
                setIfNotNull(request.getMobileNo(), entity::setMobileNo);
                setIfNotNull(request.getEmailAddress(), entity::setEmailAddress);
                break;

            // ---------------- Stage 3 : Communication ----------------
            case 3:
                setIfNotNull(request.getCommunicationDoorNo(), entity::setCommunicationDoorNo);
                setIfNotNull(request.getCommunicationLocality(), entity::setCommunicationLocality);
                setIfNotNull(request.getCommunicationStreet(), entity::setCommunicationStreet);
                setIfNotNull(request.getCommunicationVillage(), entity::setCommunicationVillage);
                setIfNotNull(request.getCommunicationMandal(), entity::setCommunicationMandal);
                setIfNotNull(request.getCommunicationDistrict(), entity::setCommunicationDistrict);
                setIfNotNull(request.getCommunicationPinCode(), entity::setCommunicationPinCode);
                break;

            // ---------------- Stage 4 : Electricity ----------------
            case 4:
                setIfNotNull(request.getTypeOfConnection(), entity::setTypeOfConnection);
                setIfNotNull(request.getServiceNo(), entity::setServiceNo);
                setIfNotNull(request.getCurrentStatus(), entity::setCurrentStatus);
                break;

            // ---------------- Stage 5 : Activity ----------------
            case 5:
                setIfNotNull(request.getUnitName(), entity::setUnitName);
                setIfNotNull(request.getEnterpriseType(), entity::setEnterpriseType);
                setIfNotNull(request.getMsmeSector(), entity::setMsmeSector);
                setIfNotNull(request.getOrganizationType(), entity::setOrganizationType);
                setIfNotNull(request.getProductDescription(), entity::setProductDescription);
                break;

            // ---------------- Stage 6 : Registration ----------------
            case 6:
                setIfNotNull(request.getUdyamRegistrationDate(), entity::setUdyamRegistrationDate);
                setIfNotNull(request.getUdyamRegistrationNo(), entity::setUdyamRegistrationNo);
                setIfNotNull(request.getGstRegNo(), entity::setGstRegNo);
                break;

            // ---------------- Stage 7 : Financial ----------------
            case 7:
                setIfNotNull(request.getBankName(), entity::setBankName);
                setIfNotNull(request.getBranchAddress(), entity::setBranchAddress);
                setIfNotNull(request.getIfscCode(), entity::setIfscCode);
                setIfNotNull(request.getUnitCostOrInvestment(), entity::setUnitCostOrInvestment);
                setIfNotNull(request.getNetTurnoverRupees(), entity::setNetTurnoverRupees);
                break;

            // ---------------- Stage 8 : Loan & Geo ----------------
            case 8:
                setIfNotNull(request.getUnitExists(), entity::setUnitExists);
                setIfNotNull(request.getUnitWorking(), entity::setUnitWorking);
                setIfNotNull(request.getBankLoanAvailed(), entity::setBankLoanAvailed);
                setIfNotNull(request.getLatitude(), entity::setLatitude);
                setIfNotNull(request.getLongitude(), entity::setLongitude);
                setIfNotNull(request.getBankLoanRequired(), entity::setBankLoanRequired);

                if (request.getSchemeNames() != null) {
                    if (entity.getSchemeNames() == null) {
                        entity.setSchemeNames(new java.util.ArrayList<>());
                    } else {
                        entity.getSchemeNames().clear();
                    }
                    entity.getSchemeNames().addAll(request.getSchemeNames());
                }

                if (request.getInterestedSchemes() != null) {
                    if (entity.getInterestedSchemes() == null) {
                        entity.setInterestedSchemes(new java.util.ArrayList<>());
                    } else {
                        entity.getInterestedSchemes().clear();
                    }
                    entity.getInterestedSchemes().addAll(request.getInterestedSchemes());
                }
                entity.setUnitExists(request.getUnitExists());
                entity.setUnitWorking(request.getUnitWorking());
                entity.setBankLoanAvailed(request.getBankLoanAvailed());
                entity.setLatitude(request.getLatitude());
                entity.setLongitude(request.getLongitude());
                entity.setBankLoanRequired(request.getBankLoanRequired());
                break;

            // ---------------- Stage -1 : Full Save ----------------
            case -1:
                // Map all fields for full save
                entity.setUnitName(request.getUnitName());
                entity.setDistrict(request.getDistrict());
                entity.setDoorNo(request.getDoorNo());
                entity.setLocalityStreet(request.getLocalityStreet());
                entity.setMandal(request.getMandal());
                entity.setVillage(request.getVillage());
                entity.setPinCode(request.getPinCode());

                String fullUnitAddress = buildUnitAddress(
                    request.getDoorNo(),
                    request.getLocalityStreet(),
                    request.getMandal(),
                    request.getVillage(),
                    request.getDistrict(),
                    request.getPinCode()
                );
                entity.setUnitAddress(fullUnitAddress);

                entity.setOfficeEmail(request.getOfficeEmail());
                entity.setOfficeContact(request.getOfficeContact());
                entity.setTotalFemaleEmployees(request.getTotalFemaleEmployees());
                entity.setTotalMaleEmployees(request.getTotalMaleEmployees());

                entity.setUnitHolderOrOwnerName(request.getUnitHolderOrOwnerName());
                entity.setCaste(request.getCaste());
                entity.setSpecialCategory(request.getSpecialCategory());
                entity.setGender(request.getGender());
                entity.setDateOfBirth(request.getDateOfBirth());
                entity.setQualification(request.getQualification());
                entity.setPanNo(request.getPanNo());
                entity.setAadharNo(request.getAadharNo());
                entity.setMobileNo(request.getMobileNo());
                entity.setEmailAddress(request.getEmailAddress());

                entity.setCommunicationDoorNo(request.getCommunicationDoorNo());
                entity.setCommunicationLocality(request.getCommunicationLocality());
                entity.setCommunicationStreet(request.getCommunicationStreet());
                entity.setCommunicationVillage(request.getCommunicationVillage());
                entity.setCommunicationMandal(request.getCommunicationMandal());
                entity.setCommunicationDistrict(request.getCommunicationDistrict());
                entity.setCommunicationPinCode(request.getCommunicationPinCode());

                entity.setTypeOfConnection(request.getTypeOfConnection());
                entity.setServiceNo(request.getServiceNo());
                entity.setCurrentStatus(request.getCurrentStatus());

                entity.setEnterpriseType(request.getEnterpriseType());
                entity.setMsmeSector(request.getMsmeSector());
                entity.setOrganizationType(request.getOrganizationType());
                entity.setProductDescription(request.getProductDescription());

                entity.setUdyamRegistrationDate(request.getUdyamRegistrationDate());
                entity.setUdyamRegistrationNo(request.getUdyamRegistrationNo());
                entity.setGstRegNo(request.getGstRegNo());

                entity.setBankName(request.getBankName());
                entity.setBranchAddress(request.getBranchAddress());
                entity.setIfscCode(request.getIfscCode());
                entity.setUnitCostOrInvestment(request.getUnitCostOrInvestment());
                entity.setNetTurnoverRupees(request.getNetTurnoverRupees());

                entity.setUnitExists(request.getUnitExists());
                entity.setUnitWorking(request.getUnitWorking());
                entity.setBankLoanAvailed(request.getBankLoanAvailed());
                entity.setLatitude(request.getLatitude());
                entity.setLongitude(request.getLongitude());
                entity.setBankLoanRequired(request.getBankLoanRequired());
                break;

            default:
                throw new IllegalArgumentException("Invalid stage number");
        }
    }

    private static <T> void setIfNotNull(T value, java.util.function.Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static String buildUnitAddress(String doorNo, String localityStreet, String mandal,
                                          String village, String district, String pinCode) {
        StringBuilder address = new StringBuilder();

        if (doorNo != null && !doorNo.trim().isEmpty()) {
            address.append(doorNo.trim()).append(", ");
        }
        if (localityStreet != null && !localityStreet.trim().isEmpty()) {
            address.append(localityStreet.trim()).append(", ");
        }
        if (village != null && !village.trim().isEmpty()) {
            address.append(village.trim()).append(", ");
        }
        if (mandal != null && !mandal.trim().isEmpty()) {
            address.append(mandal.trim()).append(", ");
        }
        if (district != null && !district.trim().isEmpty()) {
            address.append(district.trim()).append(", ");
        }
        if (pinCode != null && !pinCode.trim().isEmpty()) {
            address.append(pinCode.trim());
        }

        // Remove trailing comma and space if present
        String result = address.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }

        return result.isEmpty() ? null : result;
    }

    public static MsmeUnitDetailsDto toMsmeUnitDetailsDto(MsmeUnitDetails entity) {

        if (entity == null) {
            return null;
        }

        return MsmeUnitDetailsDto.builder()
                .msmeUnitId(entity.getMsmeUnitId())
                .stageNumber(entity.getStageNumber())
                .userId(entity.getUserId())

                .district(entity.getDistrict())
                .doorNo(entity.getDoorNo())
                .localityStreet(entity.getLocalityStreet())
                .mandal(entity.getMandal())
                .village(entity.getVillage())
                .pinCode(entity.getPinCode())
                .officeEmail(entity.getOfficeEmail())
                .officeContact(entity.getOfficeContact())
                .totalFemaleEmployees(entity.getTotalFemaleEmployees())
                .totalMaleEmployees(entity.getTotalMaleEmployees())

                .typeOfConnection(entity.getTypeOfConnection())
                .serviceNo(entity.getServiceNo())
                .currentStatus(entity.getCurrentStatus())

                .unitHolderOrOwnerName(entity.getUnitHolderOrOwnerName())
                .caste(entity.getCaste())
                .specialCategory(entity.getSpecialCategory())
                .gender(entity.getGender())
                .dateOfBirth(entity.getDateOfBirth())
                .qualification(entity.getQualification())
                .panNo(entity.getPanNo())
                .aadharNo(entity.getAadharNo())
                .mobileNo(entity.getMobileNo())
                .emailAddress(entity.getEmailAddress())

                .communicationDoorNo(entity.getCommunicationDoorNo())
                .communicationLocality(entity.getCommunicationLocality())
                .communicationStreet(entity.getCommunicationStreet())
                .communicationVillage(entity.getCommunicationVillage())
                .communicationMandal(entity.getCommunicationMandal())
                .communicationDistrict(entity.getCommunicationDistrict())
                .communicationPinCode(entity.getCommunicationPinCode())

                .unitName(entity.getUnitName())
                .enterpriseType(entity.getEnterpriseType())
                .msmeSector(entity.getMsmeSector())
                .organizationType(entity.getOrganizationType())
                .productDescription(entity.getProductDescription())

                .udyamRegistrationDate(entity.getUdyamRegistrationDate())
                .udyamRegistrationNo(entity.getUdyamRegistrationNo())
                .gstRegNo(entity.getGstRegNo())

                .bankName(entity.getBankName())
                .branchAddress(entity.getBranchAddress())
                .ifscCode(entity.getIfscCode())
                .unitCostOrInvestment(entity.getUnitCostOrInvestment())
                .netTurnoverRupees(entity.getNetTurnoverRupees())

                .unitExists(entity.getUnitExists())
                .unitWorking(entity.getUnitWorking())
                .bankLoanAvailed(entity.getBankLoanAvailed())
                .schemeNames(entity.getSchemeNames())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .bankLoanRequired(entity.getBankLoanRequired())
                .interestedSchemes(entity.getInterestedSchemes())

                .build();
    }
}
