package com.epam.esm.service.auditObject;

import com.epam.esm.dao.auditObject.AuditObjectDaoI;
import com.epam.esm.dao.auditObject.impl.AuditObjectDao2;
import com.epam.esm.model.audit.AuditObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditObjectService implements AuditObjectServiceI {
    private AuditObjectDaoI auditDao;

    @Autowired
    public AuditObjectService(AuditObjectDao2 auditDao) {
        this.auditDao = auditDao;
    }

    public void add(AuditObject object) {
        auditDao.create(object);
    }
}