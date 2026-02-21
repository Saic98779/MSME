package com.metaverse.msme.msme_unit_details.service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsmeUnitDetailsDto {

    private Long msmeUnitId;
    private Integer stageNumber;
    private String userId;

    private String unitName;
    private String district;
    private String doorNo;
    private String localityStreet;
    private String mandal;
    private String village;
    private String pinCode;
    private String officeEmail;
    private String officeContact;
    private String totalFemaleEmployees;
    private String totalMaleEmployees;

    private String typeOfConnection;
    private String serviceNo;
    private String currentStatus;

    private String unitHolderOrOwnerName;
    private String caste;
    private String specialCategory;
    private String gender;
    private String dateOfBirth;
    private String qualification;
    private String panNo;
    private String aadharNo;
    private String mobileNo;
    private String emailAddress;

    private String communicationDoorNo;
    private String communicationLocalityStreet;
    private String communicationVillage;
    private String communicationMandal;
    private String communicationDistrict;
    private String communicationPinCode;

    private String enterpriseType;
    private String msmeSector;
    private String organizationType;
    private String productDescription;

    private String udyamRegistrationDate;
    private String udyamRegistrationNo;
    private String gstRegNo;

    private String bankName;
    private String branchAddress;
    private String ifscCode;
    private String unitCostOrInvestment;
    private String netTurnoverRupees;

    private Boolean unitExists;
    private Boolean unitWorking;
    private Boolean bankLoanAvailed;
    private List<String> schemeNames;
    private String latitude;
    private String longitude;
    private Boolean bankLoanRequired;
    private List<String> interestedSchemes;
}

