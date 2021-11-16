package com.epam.esm.model.audit;

import com.epam.esm.service.auditObject.AuditObjectService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Setter
@Getter
@Component
public class AuditListener {

    private static AuditObjectService auditService;

    @Autowired
    public void init(AuditObjectService auditService) {
        AuditListener.auditService = auditService;
    }

    @PostPersist
    private void postPersist(Object o) {
        AuditObject auditObject = getObject(o);
        auditObject.setOperation("create");
        auditService.add(auditObject);
    }

    @PreRemove
    private void preDestroy(Object o) {
        AuditObject auditObject = getObject(o);
        auditObject.setOperation("delete");
        auditService.add(auditObject);
    }

    @PreUpdate
    private void preUpdate(Object o) {
        AuditObject auditObject = getObject(o);
        auditObject.setOperation("update");
        auditService.add(auditObject);
    }

    private AuditObject getObject(Object o) {
        Auditable object = (Auditable) o;
        AuditObject auditObject = object.getAudit();
        if (auditObject == null) {
            auditObject = new AuditObject();
            object.setAudit(auditObject);
        }
        return auditObject;
    }
}