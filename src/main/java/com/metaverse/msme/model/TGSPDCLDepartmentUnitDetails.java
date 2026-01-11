package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tgspdcl_department_unit_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TGSPDCLDepartmentUnitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slno")
    private Integer slno;

    @Column(name = "departmentName")
    private String departmentName;

    @Column(name = "village")
    private String village;

    @Column(name = "unitAddress")
    private String unitAddress;

    @Column(name = "extractedVillage")
    private String extractedVillage;

    @Column(name = "extracteMandal")
    private String extractedMandal;

    @Column(name = "extractedDistrict")
    private String extractedDistrict;
}
