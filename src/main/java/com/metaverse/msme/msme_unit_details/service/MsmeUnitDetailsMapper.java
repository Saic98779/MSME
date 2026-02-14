package com.metaverse.msme.msme_unit_details.service;

import com.metaverse.msme.model.MsmeUnitDetails;

public class MsmeUnitDetailsMapper {

    public static void mapUpdateMsmeUnitDetails(MsmeUnitDetailsDto request, MsmeUnitDetails entity) {

        if (request.getStageNumber() == null) {
            throw new IllegalArgumentException("Stage number is required");
        }

        entity.setStageNumber(request.getStageNumber());

        switch (request.getStageNumber()) {
            // ---------------- Stage 1 : Unit/MSME ----------------
            case 1:
                entity.setDistrict(request.getDistrict());
                entity.setUnitAddress(request.getUnitAddress());
                entity.setDoorNo(request.getDoorNo());
                entity.setStreet(request.getStreet());
                entity.setLocality(request.getLocality());
                entity.setMandal(request.getMandal());
                entity.setVillage(request.getVillage());
                entity.setPinCode(request.getPinCode());
                entity.setOfficeEmail(request.getOfficeEmail());
                entity.setOfficeContact(request.getOfficeContact());
                entity.setTotalFemaleEmployees(request.getTotalFemaleEmployees());
                entity.setTotalMaleEmployees(request.getTotalMaleEmployees());
                break;

            // ---------------- Stage 2 : Entrepreneur ----------------
            case 2:
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
                break;

            // ---------------- Stage 3 : Communication ----------------
            case 3:
                entity.setCommunicationDoorNo(request.getCommunicationDoorNo());
                entity.setCommunicationLocality(request.getCommunicationLocality());
                entity.setCommunicationStreet(request.getCommunicationStreet());
                entity.setCommunicationVillage(request.getCommunicationVillage());
                entity.setCommunicationMandal(request.getCommunicationMandal());
                entity.setCommunicationDistrict(request.getCommunicationDistrict());
                entity.setCommunicationPinCode(request.getCommunicationPinCode());
                break;

            // ---------------- Stage 4 : Electricity ----------------
            case 4:
                entity.setLtHt(request.getLtHt());
                entity.setServiceNo(request.getServiceNo());
                entity.setCurrentStatus(request.getCurrentStatus());
                break;

            // ---------------- Stage 5 : Activity ----------------
            case 5:
                entity.setUnitName(request.getUnitName());
                entity.setEnterpriseType(request.getEnterpriseType());
                entity.setMsmeSector(request.getMsmeSector());
                entity.setOrganizationType(request.getOrganizationType());
                entity.setProductDescription(request.getProductDescription());
                break;

            // ---------------- Stage 6 : Registration ----------------
            case 6:
                entity.setUdyamRegistrationDate(request.getUdyamRegistrationDate());
                entity.setUdyamRegistrationNo(request.getUdyamRegistrationNo());
                entity.setGstRegNo(request.getGstRegNo());
                break;

            // ---------------- Stage 7 : Financial ----------------
            case 7:
                entity.setBankName(request.getBankName());
                entity.setBranchAddress(request.getBranchAddress());
                entity.setIfscCode(request.getIfscCode());
                entity.setUnitCostOrInvestment(request.getUnitCostOrInvestment());
                entity.setNetTurnoverRupees(request.getNetTurnoverRupees());
                break;

            // ---------------- Stage 8 : Loan & Geo ----------------
            case 8:
                entity.setUnitExists(request.getUnitExists());
                entity.setUnitWorking(request.getUnitWorking());
                entity.setBankLoanAvailed(request.getBankLoanAvailed());
                entity.setLatitude(request.getLatitude());
                entity.setLongitude(request.getLongitude());
                entity.setBankLoanRequired(request.getBankLoanRequired());

                if (request.getSchemeNames() != null) {
                    entity.getSchemeNames().clear();
                    entity.getSchemeNames().addAll(request.getSchemeNames());
                }

                if (request.getInterestedSchemes() != null) {
                    entity.getInterestedSchemes().clear();
                    entity.getInterestedSchemes().addAll(request.getInterestedSchemes());
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid stage number");
        }
    }

    public static MsmeUnitDetailsDto toMsmeUnitDetailsDto(MsmeUnitDetails entity) {

        if (entity == null) {
            return null;
        }

        return MsmeUnitDetailsDto.builder()
                .msmeUnitId(entity.getMsmeUnitId())
                .stageNumber(entity.getStageNumber())

                .district(entity.getDistrict())
                .unitAddress(entity.getUnitAddress())
                .doorNo(entity.getDoorNo())
                .street(entity.getStreet())
                .locality(entity.getLocality())
                .mandal(entity.getMandal())
                .village(entity.getVillage())
                .pinCode(entity.getPinCode())
                .officeEmail(entity.getOfficeEmail())
                .officeContact(entity.getOfficeContact())
                .totalFemaleEmployees(entity.getTotalFemaleEmployees())
                .totalMaleEmployees(entity.getTotalMaleEmployees())

                .ltHt(entity.getLtHt())
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
