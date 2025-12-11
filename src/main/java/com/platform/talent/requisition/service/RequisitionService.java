package com.platform.talent.requisition.service;

import com.platform.talent.requisition.domain.model.Requisition;
import com.platform.talent.requisition.domain.model.RequisitionStatus;
import com.platform.talent.requisition.repository.RequisitionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;
    
    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public RequisitionService(RequisitionRepository requisitionRepository) {
        this.requisitionRepository = requisitionRepository;
    }

    @Transactional
    public Requisition createRequisition(Requisition requisition, String userId) {
        log.info("Creating new requisition: {}", requisition.getJobTitle());

        requisition.setRequisitionId(UUID.randomUUID().toString());
        requisition.setStatus(RequisitionStatus.DRAFT);
        requisition.setCreatedDate(LocalDate.now());
        requisition.setCreatedBy(userId);

        Requisition saved = requisitionRepository.save(requisition);

        publishEvent("talent.requisition.created", saved);

        return saved;
    }

    @Transactional
    public Requisition submitForApproval(String organizationId, String requisitionId, String userId) {
        Requisition requisition = getRequisition(organizationId, requisitionId);

        if (requisition.getStatus() != RequisitionStatus.DRAFT) {
            throw new IllegalStateException("Can only submit DRAFT requisitions for approval");
        }

        requisition.setStatus(RequisitionStatus.PENDING_APPROVAL);
        requisition.setLastModifiedDate(LocalDate.now());
        requisition.setLastModifiedBy(userId);

        Requisition saved = requisitionRepository.save(requisition);

        publishEvent("talent.requisition.submitted", saved);

        return saved;
    }

    @Transactional
    public Requisition approveRequisition(String organizationId, String requisitionId, String approverId) {
        Requisition requisition = getRequisition(organizationId, requisitionId);

        if (requisition.getStatus() != RequisitionStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only approve PENDING_APPROVAL requisitions");
        }

        requisition.setStatus(RequisitionStatus.APPROVED);
        requisition.setApprovedDate(LocalDate.now());
        requisition.setLastModifiedDate(LocalDate.now());
        requisition.setLastModifiedBy(approverId);

        Requisition saved = requisitionRepository.save(requisition);

        publishEvent("talent.requisition.approved", saved);

        return saved;
    }

    @Transactional
    public Requisition openRequisition(String organizationId, String requisitionId, String userId) {
        Requisition requisition = getRequisition(organizationId, requisitionId);

        if (requisition.getStatus() != RequisitionStatus.APPROVED) {
            throw new IllegalStateException("Can only open APPROVED requisitions");
        }

        requisition.setStatus(RequisitionStatus.OPEN);
        requisition.setLastModifiedDate(LocalDate.now());
        requisition.setLastModifiedBy(userId);

        Requisition saved = requisitionRepository.save(requisition);

        publishEvent("talent.requisition.opened", saved);

        return saved;
    }

    @Transactional
    public Requisition closeRequisition(String organizationId, String requisitionId, String reason, String userId) {
        Requisition requisition = getRequisition(organizationId, requisitionId);

        requisition.setStatus(RequisitionStatus.CLOSED);
        requisition.setClosedDate(LocalDate.now());
        requisition.setLastModifiedDate(LocalDate.now());
        requisition.setLastModifiedBy(userId);

        Requisition saved = requisitionRepository.save(requisition);

        publishEvent("talent.requisition.closed", Map.of(
            "requisition", saved,
            "reason", reason
        ));

        return saved;
    }

    @Transactional
    public Requisition fillRequisition(String organizationId, String requisitionId, String userId) {
        Requisition requisition = getRequisition(organizationId, requisitionId);

        requisition.setStatus(RequisitionStatus.FILLED);
        requisition.setClosedDate(LocalDate.now());
        requisition.setLastModifiedDate(LocalDate.now());
        requisition.setLastModifiedBy(userId);

        Requisition saved = requisitionRepository.save(requisition);

        publishEvent("talent.requisition.filled", saved);

        return saved;
    }

    @Transactional(readOnly = true)
    public Requisition getRequisition(String organizationId, String requisitionId) {
        return requisitionRepository.findById(requisitionId)
            .filter(r -> r.getOrganizationId().equals(organizationId))
            .orElseThrow(() -> new RuntimeException("Requisition not found"));
    }

    @Transactional(readOnly = true)
    public List<Requisition> getAllRequisitions(String organizationId) {
        return requisitionRepository.findByOrganizationId(organizationId);
    }

    @Transactional(readOnly = true)
    public List<Requisition> getOpenRequisitions(String organizationId) {
        return requisitionRepository.findByOrganizationIdAndStatus(organizationId, RequisitionStatus.OPEN);
    }

    @Transactional(readOnly = true)
    public List<Requisition> getRequisitionsByDepartment(String organizationId, String department) {
        return requisitionRepository.findByOrganizationIdAndDepartment(organizationId, department);
    }

    @Transactional(readOnly = true)
    public Long countOpenRequisitions(String organizationId) {
        return requisitionRepository.countOpenRequisitions(organizationId);
    }

    @Transactional
    public Requisition updateRequisition(String organizationId, String requisitionId, Requisition updates, String userId) {
        Requisition existing = getRequisition(organizationId, requisitionId);

        if (updates.getJobTitle() != null) existing.setJobTitle(updates.getJobTitle());
        if (updates.getJobDescription() != null) existing.setJobDescription(updates.getJobDescription());
        if (updates.getDepartment() != null) existing.setDepartment(updates.getDepartment());
        if (updates.getLocation() != null) existing.setLocation(updates.getLocation());
        if (updates.getWorkType() != null) existing.setWorkType(updates.getWorkType());
        if (updates.getEmploymentType() != null) existing.setEmploymentType(updates.getEmploymentType());
        if (updates.getNumberOfPositions() != null) existing.setNumberOfPositions(updates.getNumberOfPositions());
        if (updates.getSalaryMin() != null) existing.setSalaryMin(updates.getSalaryMin());
        if (updates.getSalaryMax() != null) existing.setSalaryMax(updates.getSalaryMax());
        if (updates.getRequiredSkills() != null) existing.setRequiredSkills(updates.getRequiredSkills());
        if (updates.getPreferredSkills() != null) existing.setPreferredSkills(updates.getPreferredSkills());
        
        existing.setLastModifiedDate(LocalDate.now());
        existing.setLastModifiedBy(userId);

        Requisition saved = requisitionRepository.save(existing);

        publishEvent("talent.requisition.updated", saved);

        return saved;
    }

    private void publishEvent(String topic, Object payload) {
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send(topic, payload);
                log.info("Published event to topic: {}", topic);
            } catch (Exception e) {
                log.error("Failed to publish event to {}", topic, e);
            }
        }
    }
}

