package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "msme_unit_details",
        indexes = {

                @Index(name = "idx_msme_district_mandal_village", columnList = "extracteddistrict, extractedmandal, extractedvillage"),
                @Index(name = "idx_msme_unitname",columnList = "unitname"),
                @Index(name = "idx_msme_ownername", columnList = "unitholderorownername"),
                @Index(name = "idx_msme_district",            columnList = "extracteddistrict"),
                @Index(name = "idx_msme_mandal",              columnList = "extractedmandal"),
                @Index(name = "idx_msme_village",             columnList = "extractedvillage")
        }
)
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

    @Column(name = "doorno", columnDefinition = "TEXT")
    private String doorNo;

    @Column(name = "locality", columnDefinition = "TEXT")
    private String localityStreet;

    @Column(name = "extractedmandal", columnDefinition = "TEXT")
    private String mandal;

    @Column(name = "extractedvillage", columnDefinition = "TEXT")
    private String village;

    @Column(name = "pincode", columnDefinition = "TEXT")
    private String pinCode;

    @Column(name = "officeemail", columnDefinition = "TEXT")
    private String officeEmail;

    @Column(name = "officecontact", columnDefinition = "TEXT")
    private String officeContact;

    @Column(name = "femaleempstotal", columnDefinition = "TEXT")
    private String totalFemaleEmployees;

    @Column(name = "maleempstotal   ", columnDefinition = "TEXT")
    private String totalMaleEmployees;

    //-----------------Electricity-------------------stage4

    @Column(name = "type_of_connection", columnDefinition = "TEXT")
    private String typeOfConnection; //type of connection

    @Column(name = "serviceno", columnDefinition = "TEXT")
    private String serviceNo;

    @Column(name = "currentstatus", columnDefinition = "TEXT")
    private String currentStatus;

    //------------------Enterprenuner--------------stage2

    @Column(name = "unitholderorownername", columnDefinition = "TEXT")
    private String unitHolderOrOwnerName;

    @Column(name = "caste", columnDefinition = "TEXT")
    private String caste;

    @Column(name = "specialcategory", columnDefinition = "TEXT")
    private String specialCategory;

    @Column(name = "gender", columnDefinition = "TEXT")
    private String gender;

    @Column(name = "dateofbirth", columnDefinition = "TEXT")
    private String dateOfBirth;

    @Column(name = "qualification", columnDefinition = "TEXT")
    private String qualification;

    @Column(name = "pan", columnDefinition = "TEXT")
    private String panNo;

    @Column(name = "aadharno", columnDefinition = "TEXT")
    private String aadharNo;

    @Column(name = "mobile_no", columnDefinition = "TEXT")
    private String mobileNo;

    @Column(name = "emailaddress", columnDefinition = "TEXT")
    private String emailAddress;

    //------Communication  / Enterprenuer address--------------- stage3


    @Column(name = "commdoorno", columnDefinition = "TEXT")
    private String communicationDoorNo;


    @Column(name = "commlocality", columnDefinition = "TEXT")
    private String communicationLocalityStreet;

    @Column(name = "commvillage", columnDefinition = "TEXT")
    private String communicationVillage;

    @Column(name = "commmandal", columnDefinition = "TEXT")
    private String communicationMandal;

    @Column(name = "commdistrict", columnDefinition = "TEXT")
    private String communicationDistrict;

    @Column(name = "commpincode", columnDefinition = "TEXT")
    private String communicationPinCode;

    //--------------------Activity---------------------stage5

    @Column(name = "unitname", columnDefinition = "TEXT")
    private String unitName;

    @Column(name = "enterprisetype", columnDefinition = "TEXT")
    private String enterpriseType;

    @Column(name = "msmesector", columnDefinition = "TEXT")
    private String msmeSector;

    @Column(name = "orgntype", columnDefinition = "TEXT")
    private String organizationType;

    @Column(name = "productdesc", length = 500, columnDefinition = "TEXT")
    private String productDescription;

    //-----------------Registration-----------------stage6

    @Column(name = "udyam_registration_date", columnDefinition = "TEXT")
    private String udyamRegistrationDate;//dateOfRegistration

    @Column(name = "udyamregistrationno", columnDefinition = "TEXT")
    private String udyamRegistrationNo;

    @Column(name = "gstregno", columnDefinition = "TEXT")
    private String gstRegNo;

    //-------------Financial--------------------------stage7
    @Column(name = "bankname", columnDefinition = "TEXT")
    private String bankName;

    @Column(name = "branchaddress", length = 500, columnDefinition = "TEXT")
    private String branchAddress;

    @Column(name = "ifsccode", columnDefinition = "TEXT")
    private String ifscCode;

    @Column(name = "unitcostorinvestment", columnDefinition = "TEXT")
    private String unitCostOrInvestment;

    @Column(name = "netturnover_rupees", columnDefinition = "TEXT")
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

    @Column(name = "is_survey_completed")
    private Boolean isSurveyCompleted;

    @Column(name = "ward", columnDefinition = "TEXT")
    private String ward;

    @Column(name = "departmentname", columnDefinition = "TEXT")
    private String departmentName;

    @Column(name = "msme_state", columnDefinition = "TEXT")
    private String msmeState;

    @Column(name = "msmedist", columnDefinition = "TEXT")
    private String msmeDist;

    @Column(name = "principalbusinessplace", columnDefinition = "TEXT")
    private String principalBusinessPlace;

    @Column(name = "institutiondetails", columnDefinition = "TEXT")
    private String institutionDetails;

    @Column(name = "purpose", columnDefinition = "TEXT")
    private String purpose;

    @Column(name = "natureofbusiness", columnDefinition = "TEXT")
    private String natureOfBusiness;

    @Column(name = "registrationunder", columnDefinition = "TEXT")
    private String registrationUnder;

    @Column(name = "registrationno", columnDefinition = "TEXT")
    private String registrationNo;

    @Column(name = "niccode", columnDefinition = "TEXT")
    private String nicCode;

    @Column(name = "incorporationdate", columnDefinition = "TEXT")
    private String incorporationDate;

    @Column(name = "commmencedate", columnDefinition = "TEXT")
    private String commenceDate;

    @Column(name = "udyamaadharrgistrationno", columnDefinition = "TEXT")
    private String udyamAadharRegistrationNo;

    @Column(name = "din", columnDefinition = "TEXT")
    private String din;

    @Column(name = "photograph", columnDefinition = "TEXT")
    private String photograph;

    @Column(name = "firstmiddlelastname", columnDefinition = "TEXT")
    private String firstMiddleLastName;

    @Column(name = "designation", columnDefinition = "TEXT")
    private String designation;

    @Column(name = "nationality", columnDefinition = "TEXT")
    private String nationality;

    @Column(name = "passport_no", columnDefinition = "TEXT")
    private String passportNo;

    @Column(name = "commlandmark", length = 500, columnDefinition = "TEXT")
    private String commLandmark;

    @Column(name = "commnameofthebuilding", columnDefinition = "TEXT")
    private String commNameOfTheBuilding;

    @Column(name = "floorno", columnDefinition = "TEXT")
    private String floorNo;

    @Column(name = "typeofloan", columnDefinition = "TEXT")
    private String typeOfLoan;

    @Column(name = "sourceofloan", columnDefinition = "TEXT")
    private String sourceOfLoan;

    @Column(name = "loanapplieddate", columnDefinition = "TEXT")
    private String loanAppliedDate;

    @Column(name = "loan_sanction_date", columnDefinition = "TEXT")
    private String loanSanctionDate;

    @Column(name = "subsidyapplicationdate", columnDefinition = "TEXT")
    private String subsidyApplicationDate;

    @Column(name = "releasedate_doc", columnDefinition = "TEXT")
    private String releaseDateDoc;

    @Column(name = "workingcapital", columnDefinition = "TEXT")
    private String workingCapital;

    @Column(name = "remarks", length = 500, columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "firmregyear", columnDefinition = "TEXT")
    private String firmRegYear;

    @Column(name = "villageid", columnDefinition = "TEXT")
    private String villageid;

    @Column(name = "commalternateno", columnDefinition = "TEXT")
    private String commAlternateNo;

    @Column(name = "load_kva", columnDefinition = "TEXT")
    private String loadKva;

    @Column(name = "unique_no", columnDefinition = "TEXT")
    private String uniqueNo;

    @Column(name = "communicationaddress", length = 500, columnDefinition = "TEXT")
    private String communicationAddress;

    private String fileUrl;

    private Boolean isCompleted; // monitoring for unit is completed or not

    private Boolean isNewUnit;

    private Boolean isDuplicate;


}
