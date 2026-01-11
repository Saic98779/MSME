package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MEMPA_Sangareddy")
@Data
public class MEMPASangareddy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "departmentname", nullable = false, length = 255)
    private String departmentName;

    @Column(name = "unitaddress", columnDefinition = "TEXT", nullable = false)
    private String unitAddress;
}
