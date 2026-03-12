package com.metaverse.msme.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Entity
@Table(name = "msme_unit_details_history")
public class MsmeUnitDetailsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "msme_unit_id", nullable = false)
    private Long msmeUnitId;

    @Column(name = "stage_number", nullable = false)
    private Integer stageNumber;

    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * Stores the stage-specific fields as a JSON object.
     * Each stageNumber corresponds to a specific StageData subclass:
     *   1 -> Stage1UnitData      (Unit/MSME address & employees)
     *   2 -> Stage2EntrepreneurData  (Entrepreneur details)
     *   3 -> Stage3CommunicationData (Communication/address)
     *   4 -> Stage4ElectricityData   (Electricity connection)
     *   5 -> Stage5ActivityData      (Activity/business details)
     *   6 -> Stage6RegistrationData  (UDYAM & GST registration)
     *   7 -> Stage7FinancialData     (Bank & financial info)
     *   8 -> Stage8OperationalData   (Operational & loan details)
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "stage_data", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> stageData;


    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "change_description", length = 500)
    private String changeDescription;
}