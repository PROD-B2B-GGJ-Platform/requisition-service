# Requisition Service - Functional Design Specification (FDS)

## Document Information

| Field | Value |
|-------|-------|
| Version | 10.0.0.1 |
| Last Updated | 2025-12-11 |
| Status | Approved |

---

## 1. Functional Requirements

### FR-001: Create Requisition

Hiring manager creates job requisition.

**Input:**
- jobTitle (required)
- jobDescription
- departmentId
- jobType (NEW, REPLACEMENT, EXPANSION)
- employmentType
- headcount
- salaryMin, salaryMax, currency
- budgetCode
- justification (required)
- urgency

**Business Rules:**
- Starts in DRAFT status
- Required fields validated
- Budget code verified

---

### FR-002: Submit for Approval

Submit requisition for approval workflow.

**Business Rules:**
- All required fields must be filled
- Determines approval chain based on criteria
- Notifies first-level approver

---

### FR-003: Approve Requisition

Approver approves at their level.

**Business Rules:**
- Only pending approvers can approve
- Moves to next level or completes
- Comments optional

---

### FR-004: Reject Requisition

Approver rejects requisition.

**Business Rules:**
- Rejection reason required
- Workflow stops
- Requester notified

---

### FR-005: Convert to Job Posting

Create job posting from approved requisition.

**Business Rules:**
- Requisition must be APPROVED
- Calls Job Posting Service API
- Links job posting to requisition

---

## 2. Approval Chain Logic

| Condition | Required Approvals |
|-----------|-------------------|
| Base | Department Head, HR Manager |
| Salary > $100k | + Finance Director |
| Headcount > 5 | + VP/Executive |
| Executive hire | + CEO |

---

## 3. Acceptance Criteria

- [ ] Requisitions created with validation
- [ ] Multi-level approval enforced
- [ ] Rejection stops workflow
- [ ] Job posting created on conversion
- [ ] All stakeholders notified

