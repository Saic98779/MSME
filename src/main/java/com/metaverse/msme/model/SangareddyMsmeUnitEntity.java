package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "msme_unit_details_sangareddy")
@Getter
@Setter
@Data
public class SangareddyMsmeUnitEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "slno")
    private Integer slno;

    @Column(length = 100)
    private String uniqueNo;
    private String departmentName;

    @Column(name = "msmeState")
    private String msmeState;

    @Column(name = "msmedist")
    private String msmeDist;

    @Column(name = "msmesector")
    private String msmeSector;

    @Column( name = "unitname", columnDefinition = "TEXT")
    private String unitName;

    private String category;

    @Column(name = "unitaddress", columnDefinition = "TEXT")
    private String unitAddress;

    @Column(name = "doorno")
    private String doorNo;

    private String locality;
    private String street;
    private String village;
    @Column(name = "village_id")
    private String villageId;
    private String ward;
    private String mandal;
    private String district;
    private String pinCode;

    @Column(name = "officeemail")
    private String officeEmail;

    @Column(name = "officecontact")
    private String officeContact;
    @Column(name = "principabusinessplace")
    private String principalBusinessPlace;

    // ---------- EMPLOYEES ----------
    @Column(name = "femaleempstotal")
    private String femaleEmpsTotal;
    private String maleEmpsTotal;


    // ---------- BUSINESS ----------
    @Column(name = "institutiondetails", columnDefinition = "TEXT")
    private String institutionDetails;

    private String purpose;

    @Column(name = "orgntype")
    private String orgnType;

    @Column(name = "enterprisetype")
    private String enterpriseType;

    @Column(name = "natureofbusiness")
    private String natureOfBusiness;

    @Column(name = "productdesc", columnDefinition = "TEXT")
    private String productDesc;

    @Column(name = "registrationunder")
    private String registrationUnder;

    @Column(name = "registrationno")
    private String registrationNo;

    private String dateOfRegistration;
    private String udyamRegistrationNo;
    private String nicCode;
    private String incorporationDate;
    private String commmenceDate;
    private String udyamAadharRgistrationNo;
    private String gstRegNo;
    private String din;

    private String photograph;

    // ---------- OWNER ----------
    private String unitHolderOrOwnerName;
    private String firstMiddleLastName;
    private String designation;
    private String caste;
    private String specialCategory;
    private String gender;
    private String dateofBirth;
    private String qualification;
    private String nationality;
    private String pan;
    private String aadharNo;
    private String passportNo;

    // ---------- COMMUNICATION ADDRESS ----------
    @Column(columnDefinition = "TEXT")
    private String communicationAddress;

    private String commDoorNo;
    private String commLocality;
    private String commStreet;
    private String commLandmark;
    private String commNameofthebuilding;
    private String floorNo;
    private String commVillage;
    private String commMandal;
    private String commDistrict;
    private String commPINcode;
    private String commMobileNo;
    private String commAlternateNo;
    private String emailAddress;

    // ---------- POWER ----------
    private String ltHt;
    private String serviceNo;

    // ---------- STATUS ----------
    private String currentStatus;

    // ---------- LOAN ----------
    private String typeofLoan;
    private String sourceofLoan;
    private String loanAppliedDate;
    private String loanSanctionDate;
    private String subsidyApplicationDate;
    private String bankName;

    @Column(columnDefinition = "TEXT")
    private String branchNameAddress;

    private String ifsCcode;
    private String releaseDateDoc;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private String firmRegYear;

    // ---------- EXTRACTION ----------
    private String extractedVillage;
    private String extractedMandal;
    private String extractedDistrict;
    private String processedFlag;

    // ---------- MUNICIPAL ----------
    private String muncipality;
    private String ulbname;

}