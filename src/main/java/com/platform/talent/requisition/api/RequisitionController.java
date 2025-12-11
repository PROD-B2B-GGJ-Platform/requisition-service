package com.platform.talent.requisition.api;

import com.platform.talent.requisition.domain.model.Requisition;
import com.platform.talent.requisition.service.RequisitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requisitions")
@RequiredArgsConstructor
public class RequisitionController {

    private final RequisitionService requisitionService;

    @PostMapping
    public ResponseEntity<Requisition> createRequisition(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @RequestBody Requisition requisition
    ) {
        requisition.setOrganizationId(organizationId);
        Requisition created = requisitionService.createRequisition(requisition, userId);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Requisition>> getAllRequisitions(
        @RequestHeader("X-Organization-Id") String organizationId
    ) {
        List<Requisition> requisitions = requisitionService.getAllRequisitions(organizationId);
        return ResponseEntity.ok(requisitions);
    }

    @GetMapping("/{requisitionId}")
    public ResponseEntity<Requisition> getRequisition(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String requisitionId
    ) {
        Requisition requisition = requisitionService.getRequisition(organizationId, requisitionId);
        return ResponseEntity.ok(requisition);
    }

    @GetMapping("/open")
    public ResponseEntity<List<Requisition>> getOpenRequisitions(
        @RequestHeader("X-Organization-Id") String organizationId
    ) {
        List<Requisition> requisitions = requisitionService.getOpenRequisitions(organizationId);
        return ResponseEntity.ok(requisitions);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Requisition>> getByDepartment(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String department
    ) {
        List<Requisition> requisitions = requisitionService.getRequisitionsByDepartment(organizationId, department);
        return ResponseEntity.ok(requisitions);
    }

    @GetMapping("/count/open")
    public ResponseEntity<Map<String, Long>> countOpen(
        @RequestHeader("X-Organization-Id") String organizationId
    ) {
        Long count = requisitionService.countOpenRequisitions(organizationId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PutMapping("/{requisitionId}")
    public ResponseEntity<Requisition> updateRequisition(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @PathVariable String requisitionId,
        @RequestBody Requisition updates
    ) {
        Requisition updated = requisitionService.updateRequisition(organizationId, requisitionId, updates, userId);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{requisitionId}/submit")
    public ResponseEntity<Requisition> submitForApproval(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @PathVariable String requisitionId
    ) {
        Requisition submitted = requisitionService.submitForApproval(organizationId, requisitionId, userId);
        return ResponseEntity.ok(submitted);
    }

    @PostMapping("/{requisitionId}/approve")
    public ResponseEntity<Requisition> approve(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @PathVariable String requisitionId
    ) {
        Requisition approved = requisitionService.approveRequisition(organizationId, requisitionId, userId);
        return ResponseEntity.ok(approved);
    }

    @PostMapping("/{requisitionId}/open")
    public ResponseEntity<Requisition> openRequisition(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @PathVariable String requisitionId
    ) {
        Requisition opened = requisitionService.openRequisition(organizationId, requisitionId, userId);
        return ResponseEntity.ok(opened);
    }

    @PostMapping("/{requisitionId}/close")
    public ResponseEntity<Requisition> closeRequisition(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @PathVariable String requisitionId,
        @RequestBody Map<String, String> body
    ) {
        String reason = body.getOrDefault("reason", "Closed by user");
        Requisition closed = requisitionService.closeRequisition(organizationId, requisitionId, reason, userId);
        return ResponseEntity.ok(closed);
    }

    @PostMapping("/{requisitionId}/fill")
    public ResponseEntity<Requisition> fillRequisition(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @PathVariable String requisitionId
    ) {
        Requisition filled = requisitionService.fillRequisition(organizationId, requisitionId, userId);
        return ResponseEntity.ok(filled);
    }
}

