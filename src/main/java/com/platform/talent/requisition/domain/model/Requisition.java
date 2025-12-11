package com.platform.talent.requisition.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "requisitions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Requisition {
    
    @Id
    private String requisitionId;
    
    @Column(nullable = false)
    private String organizationId;
    
    @Column(nullable = false)
    private String jobTitle;
    
    @Column(length = 4000)
    private String jobDescription;
    
    @Column(nullable = false)
    private String department;
    
    private String location;
    private String workType; // REMOTE, ONSITE, HYBRID
    private String employmentType; // FULL_TIME, PART_TIME, CONTRACT
    
    private Integer numberOfPositions;
    
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String currency;
    
    @Column(nullable = false)
    private String hiringManagerId;
    
    private String recruiterId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequisitionStatus status;
    
    private Integer priority; // 1=Critical, 2=High, 3=Medium, 4=Low
    
    private LocalDate targetStartDate;
    private LocalDate approvedDate;
    private LocalDate closedDate;
    
    @Column(length = 2000)
    private String requiredSkills;
    
    @Column(length = 2000)
    private String preferredSkills;
    
    private Integer experienceMin;
    private Integer experienceMax;
    
    private String educationLevel;
    
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}

