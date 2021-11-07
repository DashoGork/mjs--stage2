package com.epam.esm.service.auditObject;

import com.epam.esm.dao.auditObject.AuditObjectDao;
import com.epam.esm.model.audit.AuditObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditObjectService implements AuditObjectServiceI {
    private AuditObjectDao auditDao;

    @Autowired
    public AuditObjectService(AuditObjectDao auditDao) {
        this.auditDao = auditDao;
    }

    public void add(AuditObject object) {
        auditDao.save(object);
    }
}