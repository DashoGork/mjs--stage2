package com.epam.esm.service.auditObject;

import com.epam.esm.dao.auditObject.AuditObjectDao;
import com.epam.esm.model.audit.AuditObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuditObjectServiceTest {
    @Mock
    private AuditObjectDao dao;

    private AuditObjectService service;
    private AuditObject auditObject;

    @Before
    public void setUp() throws Exception {
        service = new AuditObjectService(dao);
        auditObject = new AuditObject();
        auditObject.setOperation("delete");
    }

    @Test
    public void add() {
        when(dao.save(auditObject)).thenReturn(auditObject);
        service.add(auditObject);
        verify(dao).save(auditObject);
    }
}