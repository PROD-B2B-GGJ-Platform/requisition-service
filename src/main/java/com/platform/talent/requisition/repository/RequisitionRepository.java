package com.platform.talent.requisition.repository;

import com.platform.talent.requisition.domain.model.Requisition;
import com.platform.talent.requisition.domain.model.RequisitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequisitionRepository extends JpaRepository<Requisition, String> {
    List<Requisition> findByOrganizationId(String organizationId);
    List<Requisition> findByOrganizationIdAndStatus(String organizationId, RequisitionStatus status);
    List<Requisition> findByOrganizationIdAndDepartment(String organizationId, String department);
    List<Requisition> findByHiringManagerId(String hiringManagerId);
    List<Requisition> findByRecruiterId(String recruiterId);
    
    @Query("SELECT r FROM Requisition r WHERE r.organizationId = :orgId AND r.status IN ('OPEN', 'APPROVED')")
    List<Requisition> findActiveRequisitions(String orgId);
    
    @Query("SELECT COUNT(r) FROM Requisition r WHERE r.organizationId = :orgId AND r.status = 'OPEN'")
    Long countOpenRequisitions(String orgId);
}

