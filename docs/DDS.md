# Requisition Service - Design Document Specification (DDS)

## Document Information

| Field | Value |
|-------|-------|
| Version | 10.0.0.1 |
| Last Updated | 2025-12-11 |
| Status | Approved |
| Owner | Talent & Recruitment Team |

---

## 1. Overview

The Requisition Service manages job requisition workflows, enabling hiring managers to request new positions through an approval process before job postings are created.

### 1.1 Key Features

- Requisition creation and submission
- Multi-level approval workflow
- Budget validation
- Integration with Job Posting Service
- Headcount tracking

---

## 2. Architecture

### 2.1 Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Runtime | Java | 21+ |
| Framework | Spring Boot | 3.2.0 |
| Database | PostgreSQL | 15+ |
| Port | 8097 | |

---

## 3. Data Model

### 3.1 Database Schema

#### Table: requisitions

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| organization_id | UUID | Multi-tenant isolation |
| department_id | UUID | FK to departments |
| requester_id | UUID | Hiring manager |
| job_title | VARCHAR(255) | Position title |
| job_description | TEXT | Position description |
| job_type | VARCHAR(50) | NEW, REPLACEMENT, EXPANSION |
| employment_type | VARCHAR(50) | FULL_TIME, PART_TIME, CONTRACT |
| headcount | INTEGER | Number of positions |
| salary_min | DECIMAL(15,2) | Salary range min |
| salary_max | DECIMAL(15,2) | Salary range max |
| currency | VARCHAR(3) | Currency code |
| budget_code | VARCHAR(50) | Budget allocation code |
| justification | TEXT | Business justification |
| urgency | VARCHAR(20) | LOW, MEDIUM, HIGH, CRITICAL |
| status | VARCHAR(20) | DRAFT, SUBMITTED, APPROVED, REJECTED, CANCELLED |
| approved_by | UUID | Final approver |
| approved_at | TIMESTAMP | Approval timestamp |
| job_posting_id | UUID | Created job posting |
| created_at | TIMESTAMP | Creation time |

#### Table: requisition_approvals

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| requisition_id | UUID | FK to requisitions |
| approver_id | UUID | Approver user |
| approval_level | INTEGER | 1, 2, 3 |
| status | VARCHAR(20) | PENDING, APPROVED, REJECTED |
| comments | TEXT | Approval comments |
| actioned_at | TIMESTAMP | When actioned |

---

## 4. API Design

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/requisitions` | Create requisition |
| GET | `/api/v1/requisitions/{id}` | Get requisition |
| PUT | `/api/v1/requisitions/{id}` | Update requisition |
| POST | `/api/v1/requisitions/{id}/submit` | Submit for approval |
| POST | `/api/v1/requisitions/{id}/approve` | Approve requisition |
| POST | `/api/v1/requisitions/{id}/reject` | Reject requisition |
| POST | `/api/v1/requisitions/{id}/convert-to-job` | Create job posting |
| GET | `/api/v1/requisitions/pending` | Pending approvals |

---

## 5. Approval Workflow

### 5.1 Approval Levels

| Level | Approver | Criteria |
|-------|----------|----------|
| 1 | Department Head | All requisitions |
| 2 | HR Manager | All requisitions |
| 3 | Finance | If salary > threshold |
| 4 | Executive | If headcount > 5 |

### 5.2 Workflow

```
DRAFT → SUBMITTED → [Level 1] → [Level 2] → [Level 3?] → [Level 4?] → APPROVED
                           ↓           ↓            ↓            ↓
                        REJECTED    REJECTED     REJECTED     REJECTED
```

---

## 6. Events Published

| Topic | Trigger |
|-------|---------|
| talent.requisition.submitted | Submitted for approval |
| talent.requisition.approved | Fully approved |
| talent.requisition.rejected | Rejected at any level |
| talent.requisition.job-created | Converted to job posting |

---

## 7. Integration Points

### 7.1 Outbound

| Service | Purpose |
|---------|---------|
| Job Posting Service | Create job from approved requisition |
| Budget Service | Validate budget availability |
| Notification Service | Alert approvers |

---

## 8. References

- [Spring State Machine](https://spring.io/projects/spring-statemachine)

