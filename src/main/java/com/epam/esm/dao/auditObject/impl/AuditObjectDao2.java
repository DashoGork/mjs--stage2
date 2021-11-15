package com.epam.esm.dao.auditObject.impl;

import com.epam.esm.dao.auditObject.AuditObjectDaoI;
import com.epam.esm.model.audit.AuditObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@AllArgsConstructor
@Repository
public class AuditObjectDao2 implements AuditObjectDaoI {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void create(AuditObject auditObject) {
        entityManager.persist(auditObject);
    }
}
