package com.epam.esm.model.audit;

public interface Auditable {
    AuditObject getAudit();

    void setAudit(AuditObject audit);
}
