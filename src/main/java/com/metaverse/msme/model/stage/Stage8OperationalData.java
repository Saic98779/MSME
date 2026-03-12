package com.metaverse.msme.model.stage;

import lombok.Data;

@Data
public class Stage8OperationalData {

    private Boolean unitExists;
    private Boolean unitWorking;
    private String latitude;
    private String longitude;
    private Boolean bankLoanAvailed;
    private Boolean bankLoanRequired;
    private String typeOfLoan;
    private String sourceOfLoan;
    private String loanAppliedDate;
    private String loanSanctionDate;
    private String subsidyApplicationDate;
    private String releaseDateDoc;
    private String remarks;
}