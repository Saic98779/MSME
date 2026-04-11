package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sector")
@Data
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sector_id")
    private Long id;

    @Column(name = "sector_name", nullable = false, unique = true)
    private String sectorName;
}
