package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "msme_unit_details")
public class MsmeUnitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msme_unit_id")
    private Long msmeUnitId;

    @Column(name = "stage_number")
    private Integer stageNumber;

    @Column(name = "user_id")
    private String userId;

    //---------------Unit/MSME----------------- stage1

    @Column(name = "district")
    private String district;

    @Column(name = "unit_address", length = 500)
    private String unitAddress;

    @Column(name = "door_no")
    private String doorNo;

    @Column(name = "locality_street")
    private String localityStreet;

    @Column(name = "mandal")
    private String mandal;

    @Column(name = "village")
    private String village;

    @Column(name = "pincode")
    private String pinCode;

    @Column(name = "office_email")
    private String officeEmail;

    @Column(name = "office_contact")
    private String officeContact;

    @Column(name = "total_female_employees")
    private String totalFemaleEmployees;

    @Column(name = "total_male_employees")
    private String totalMaleEmployees;

    //-----------------Electricity-------------------stage4

    @Column(name = "type_of_connection")
    private String typeOfConnection; //type of connection

    @Column(name = "service_no")
    private String serviceNo;

    @Column(name = "current_status")
    private String currentStatus;

    //------------------Enterprenuner--------------stage2

    @Column(name = "unit_holder_or_owner_name")
    private String unitHolderOrOwnerName;

    @Column(name = "caste")
    private String caste;

    @Column(name = "special_category")
    private String specialCategory;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "pan_no")
    private String panNo;

    @Column(name = "aadhar_no")
    private String aadharNo;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "emailaddress")
    private String emailAddress;

    //------Communication  / Enterprenuer address--------------- stage3


    @Column(name = "communication_Doorno")
    private String communicationDoorNo;

    @Column(name = "communication_locality")
    private String communicationLocality;

    @Column(name = "communication_street")
    private String communicationStreet;

    @Column(name = "communication_village")
    private String communicationVillage;

    @Column(name = "communication_mandal")
    private String communicationMandal;

    @Column(name = "communication_district")
    private String communicationDistrict;

    @Column(name = "communication_pincode")
    private String communicationPinCode;

    //--------------------Activity---------------------stage5

    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "enterprise_type")
    private String enterpriseType;

    @Column(name = "msmes_sector")
    private String msmeSector;

    @Column(name = "organization_type")
    private String organizationType;

    @Column(name = "product_description", length = 500)
    private String productDescription;

    //-----------------Registration-----------------stage6

    @Column(name = "udyam_registration_date")
    private String udyamRegistrationDate;//dateOfRegistration

    @Column(name = "udyam_registration_no")
    private String udyamRegistrationNo;

    @Column(name = "gstregno")
    private String gstRegNo;

    //-------------Financial--------------------------stage7
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "branch_address", length = 500)
    private String branchAddress;

    @Column(name = "ifsccode")
    private String ifscCode;

    @Column(name = "unit_cost_or_investment")
    private String unitCostOrInvestment;

    @Column(name = "net_turnover_rupees")
    private String netTurnoverRupees;

    //----------New fields to be captured----------------stage8

    @Column(name = "unit_exists")
    private Boolean unitExists;

    @Column(name = "unit_working")
    private Boolean unitWorking;

    @Column(name = "bank_loan_availed")
    private Boolean bankLoanAvailed;

    @ElementCollection
    @CollectionTable(name = "msme_schemes_availed",
            joinColumns = @JoinColumn(name = "slno"))
    @Column(name = "scheme_name")
    private List<String> schemeNames;

    @Column(name = "lattitude")
    private String latitude;

    @Column(name = "longitute")
    private String longitude;

    @Column(name = "bank_loan_required")
    private Boolean bankLoanRequired;

    @ElementCollection
    @CollectionTable(name = "msme_schemes_interested",
            joinColumns = @JoinColumn(name = "slno"))
    @Column(name = "scheme_name")
    private List<String> interestedSchemes;


    @Column(name = "category")
    private String category;

    @Column(name = "ward")
    private String ward;

    @Column(name = "department_name")
    private String departmentName;

    @Column(name = "msme_state")
    private String msmeState;

    @Column(name = "msmes_dist")
    private String msmeDist;

    @Column(name = "principal_business_place")
    private String principalBusinessPlace;

    @Column(name = "institution_details")
    private String institutionDetails;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "nature_of_business")
    private String natureOfBusiness;

    @Column(name = "registration_under")
    private String registrationUnder;

    @Column(name = "registration_no")
    private String registrationNo;

    @Column(name = "nic_code")
    private String nicCode;

    @Column(name = "incorporation_date")
    private String incorporationDate;

    @Column(name = "commmence_date")
    private String commenceDate;

    @Column(name = "udyam_aadhar_rgistration_no")
    private String udyamAadharRegistrationNo;

    @Column(name = "din")
    private String din;

    @Column(name = "photograph")
    private String photograph;

    @Column(name = "first_middle_last_Name")
    private String firstMiddleLastName;

    @Column(name = "designation")
    private String designation;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "passport_no")
    private String passportNo;

    @Column(name = "comm_landmark", length = 500)
    private String commLandmark;

    @Column(name = "comm_name_of_the_building")
    private String commNameOfTheBuilding;

    @Column(name = "floor_no")
    private String floorNo;

    @Column(name = "type_of_loan")
    private String typeOfLoan;

    @Column(name = "source_of_loan")
    private String sourceOfLoan;

    @Column(name = "loan_applied_date")
    private String loanAppliedDate;

    @Column(name = "loan_sanction_date")
    private String loanSanctionDate;

    @Column(name = "subsidy_application_date")
    private String subsidyApplicationDate;

    @Column(name = "release_date_doc")
    private String releaseDateDoc;

    @Column(name = "working_capital")
    private String workingCapital;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "firm_reg_year")
    private String firmRegYear;

    @Column(name = "village_id")
    private String villageid;

    @Column(name = "comm_alternate_no")
    private String commAlternateNo;

    @Column(name = "load_kva")
    private String loadKva;

    @Column(name = "unique_no")
    private String uniqueNo;

    @Column(name = "communicationaddress", length = 500)
    private String communicationAddress;

}
