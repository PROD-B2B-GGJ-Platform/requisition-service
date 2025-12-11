# Requisition Service - Help Guide

## Quick Start

```bash
git clone https://github.com/PROD-B2B-GGJ-Platform/requisition-service.git
cd requisition-service

export DB_HOST=localhost
mvn spring-boot:run
```

Access: http://localhost:8097/swagger-ui.html

---

## API Examples

### Create Requisition

```bash
curl -X POST http://localhost:8097/api/v1/requisitions \
  -H "Content-Type: application/json" \
  -d '{
    "jobTitle": "Senior Software Engineer",
    "jobDescription": "Looking for experienced engineer...",
    "departmentId": "dept-uuid",
    "jobType": "NEW",
    "employmentType": "FULL_TIME",
    "headcount": 2,
    "salaryMin": 120000,
    "salaryMax": 180000,
    "currency": "USD",
    "budgetCode": "ENG-2025-001",
    "justification": "Team expansion to support new product launch",
    "urgency": "HIGH"
  }'
```

### Submit for Approval

```bash
curl -X POST http://localhost:8097/api/v1/requisitions/{id}/submit
```

### Approve Requisition

```bash
curl -X POST http://localhost:8097/api/v1/requisitions/{id}/approve \
  -H "Content-Type: application/json" \
  -d '{
    "comments": "Approved - budget available"
  }'
```

### Convert to Job Posting

```bash
curl -X POST http://localhost:8097/api/v1/requisitions/{id}/convert-to-job
```

---

## Requisition Types

| Type | Description |
|------|-------------|
| NEW | New position creation |
| REPLACEMENT | Replacing departed employee |
| EXPANSION | Team growth/expansion |

---

## Approval Flow

```
Hiring Manager → Department Head → HR → [Finance] → [Executive]
```

Finance approval required for salary > $100k
Executive approval required for headcount > 5

---

## Support

GitHub: https://github.com/PROD-B2B-GGJ-Platform/requisition-service

