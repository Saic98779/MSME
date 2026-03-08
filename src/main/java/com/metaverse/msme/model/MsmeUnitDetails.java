package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "msme_unit_details")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MsmeUnitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msme_unit_id")
    private Long msmeUnitId;

    @Column(name = "stage_number")
    private Integer stageNumber;

    @Column(name = "user_id", columnDefinition = "TEXT")
    private String userId;

    //---------------Unit/MSME----------------- stage1

    @Column(name = "extracteddistrict", columnDefinition = "TEXT")
    private String district;

    @Column(name = "unit_address", length = 500, columnDefinition = "TEXT")
    private String unitAddress;

    @Column(name = "door_no", columnDefinition = "TEXT")
    private String doorNo;

    @Column(name = "locality_street", columnDefinition = "TEXT")
    private String localityStreet;

    @Column(name = "extractedmandal", columnDefinition = "TEXT")
    private String mandal;

    @Column(name = "extractedvillage", columnDefinition = "TEXT")
    private String village;

    @Column(name = "pincode", columnDefinition = "TEXT")
    private String pinCode;

    @Column(name = "office_email", columnDefinition = "TEXT")
    private String officeEmail;

    @Column(name = "office_contact", columnDefinition = "TEXT")
    private String officeContact;

    @Column(name = "total_female_employees", columnDefinition = "TEXT")
    private String totalFemaleEmployees;

    @Column(name = "total_male_employees", columnDefinition = "TEXT")
    private String totalMaleEmployees;

    //-----------------Electricity-------------------stage4

    @Column(name = "type_of_connection", columnDefinition = "TEXT")
    private String typeOfConnection; //type of connection

    @Column(name = "service_no", columnDefinition = "TEXT")
    private String serviceNo;

    @Column(name = "current_status", columnDefinition = "TEXT")
    private String currentStatus;

    //------------------Enterprenuner--------------stage2

    @Column(name = "unit_holder_or_owner_name", columnDefinition = "TEXT")
    private String unitHolderOrOwnerName;

    @Column(name = "caste", columnDefinition = "TEXT")
    private String caste;

    @Column(name = "special_category", columnDefinition = "TEXT")
    private String specialCategory;

    @Column(name = "gender", columnDefinition = "TEXT")
    private String gender;

    @Column(name = "date_of_birth", columnDefinition = "TEXT")
    private String dateOfBirth;

    @Column(name = "qualification", columnDefinition = "TEXT")
    private String qualification;

    @Column(name = "pan_no", columnDefinition = "TEXT")
    private String panNo;

    @Column(name = "aadhar_no", columnDefinition = "TEXT")
    private String aadharNo;

    @Column(name = "mobile_no", columnDefinition = "TEXT")
    private String mobileNo;

    @Column(name = "emailaddress", columnDefinition = "TEXT")
    private String emailAddress;

    //------Communication  / Enterprenuer address--------------- stage3


    @Column(name = "communication_Doorno", columnDefinition = "TEXT")
    private String communicationDoorNo;


    @Column(name = "communication_locality_street", columnDefinition = "TEXT")
    private String communicationLocalityStreet;

    @Column(name = "communication_village", columnDefinition = "TEXT")
    private String communicationVillage;

    @Column(name = "communication_mandal", columnDefinition = "TEXT")
    private String communicationMandal;

    @Column(name = "communication_district", columnDefinition = "TEXT")
    private String communicationDistrict;

    @Column(name = "communication_pincode", columnDefinition = "TEXT")
    private String communicationPinCode;

    //--------------------Activity---------------------stage5

    @Column(name = "unit_name", columnDefinition = "TEXT")
    private String unitName;

    @Column(name = "enterprise_type", columnDefinition = "TEXT")
    private String enterpriseType;

    @Column(name = "msmes_sector", columnDefinition = "TEXT")
    private String msmeSector;

    @Column(name = "organization_type", columnDefinition = "TEXT")
    private String organizationType;

    @Column(name = "product_description", length = 500, columnDefinition = "TEXT")
    private String productDescription;

    //-----------------Registration-----------------stage6

    @Column(name = "udyam_registration_date", columnDefinition = "TEXT")
    private String udyamRegistrationDate;//dateOfRegistration

    @Column(name = "udyam_registration_no", columnDefinition = "TEXT")
    private String udyamRegistrationNo;

    @Column(name = "gstregno", columnDefinition = "TEXT")
    private String gstRegNo;

    //-------------Financial--------------------------stage7
    @Column(name = "bank_name", columnDefinition = "TEXT")
    private String bankName;

    @Column(name = "branch_address", length = 500, columnDefinition = "TEXT")
    private String branchAddress;

    @Column(name = "ifsccode", columnDefinition = "TEXT")
    private String ifscCode;

    @Column(name = "unit_cost_or_investment", columnDefinition = "TEXT")
    private String unitCostOrInvestment;

    @Column(name = "net_turnover_rupees", columnDefinition = "TEXT")
    private String netTurnoverRupees;

    //----------New fields to be captured----------------stage8

    @Column(name = "unit_exists", columnDefinition = "TEXT")
    private Boolean unitExists;

    @Column(name = "unit_working", columnDefinition = "TEXT")
    private Boolean unitWorking;

    @Column(name = "bank_loan_availed", columnDefinition = "TEXT")
    private Boolean bankLoanAvailed;

    @ElementCollection
    @CollectionTable(name = "msme_schemes_availed",
            joinColumns = @JoinColumn(name = "msme_unit_id"))
    @Column(name = "scheme_name", columnDefinition = "TEXT")
    private List<String> schemeNames;

    @Column(name = "lattitude", columnDefinition = "TEXT")
    private String latitude;

    @Column(name = "longitute", columnDefinition = "TEXT")
    private String longitude;

    @Column(name = "bank_loan_required", columnDefinition = "TEXT")
    private Boolean bankLoanRequired;

    @ElementCollection
    @CollectionTable(name = "msme_schemes_interested",
            joinColumns = @JoinColumn(name = "msme_unit_id"))
    @Column(name = "scheme_name", columnDefinition = "TEXT")
    private List<String> interestedSchemes;


    @Column(name = "category", columnDefinition = "TEXT")
    private String category;

    @Column(name = "ward", columnDefinition = "TEXT")
    private String ward;

    @Column(name = "department_name", columnDefinition = "TEXT")
    private String departmentName;

    @Column(name = "msme_state", columnDefinition = "TEXT")
    private String msmeState;

    @Column(name = "msmes_dist", columnDefinition = "TEXT")
    private String msmeDist;

    @Column(name = "principal_business_place", columnDefinition = "TEXT")
    private String principalBusinessPlace;

    @Column(name = "institution_details", columnDefinition = "TEXT")
    private String institutionDetails;

    @Column(name = "purpose", columnDefinition = "TEXT")
    private String purpose;

    @Column(name = "nature_of_business", columnDefinition = "TEXT")
    private String natureOfBusiness;

    @Column(name = "registration_under", columnDefinition = "TEXT")
    private String registrationUnder;

    @Column(name = "registration_no", columnDefinition = "TEXT")
    private String registrationNo;

    @Column(name = "nic_code", columnDefinition = "TEXT")
    private String nicCode;

    @Column(name = "incorporation_date", columnDefinition = "TEXT")
    private String incorporationDate;

    @Column(name = "commmence_date", columnDefinition = "TEXT")
    private String commenceDate;

    @Column(name = "udyam_aadhar_rgistration_no", columnDefinition = "TEXT")
    private String udyamAadharRegistrationNo;

    @Column(name = "din", columnDefinition = "TEXT")
    private String din;

    @Column(name = "photograph", columnDefinition = "TEXT")
    private String photograph;

    @Column(name = "first_middle_last_Name", columnDefinition = "TEXT")
    private String firstMiddleLastName;

    @Column(name = "designation", columnDefinition = "TEXT")
    private String designation;

    @Column(name = "nationality", columnDefinition = "TEXT")
    private String nationality;

    @Column(name = "passport_no", columnDefinition = "TEXT")
    private String passportNo;

    @Column(name = "comm_landmark", length = 500, columnDefinition = "TEXT")
    private String commLandmark;

    @Column(name = "comm_name_of_the_building", columnDefinition = "TEXT")
    private String commNameOfTheBuilding;

    @Column(name = "floor_no", columnDefinition = "TEXT")
    private String floorNo;

    @Column(name = "type_of_loan", columnDefinition = "TEXT")
    private String typeOfLoan;

    @Column(name = "source_of_loan", columnDefinition = "TEXT")
    private String sourceOfLoan;

    @Column(name = "loan_applied_date", columnDefinition = "TEXT")
    private String loanAppliedDate;

    @Column(name = "loan_sanction_date", columnDefinition = "TEXT")
    private String loanSanctionDate;

    @Column(name = "subsidy_application_date", columnDefinition = "TEXT")
    private String subsidyApplicationDate;

    @Column(name = "release_date_doc", columnDefinition = "TEXT")
    private String releaseDateDoc;

    @Column(name = "working_capital", columnDefinition = "TEXT")
    private String workingCapital;

    @Column(name = "remarks", length = 500, columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "firm_reg_year", columnDefinition = "TEXT")
    private String firmRegYear;

    @Column(name = "village_id", columnDefinition = "TEXT")
    private String villageid;

    @Column(name = "comm_alternate_no", columnDefinition = "TEXT")
    private String commAlternateNo;

    @Column(name = "load_kva", columnDefinition = "TEXT")
    private String loadKva;

    @Column(name = "unique_no", columnDefinition = "TEXT")
    private String uniqueNo;

    @Column(name = "communicationaddress", length = 500, columnDefinition = "TEXT")
    private String communicationAddress;

}
