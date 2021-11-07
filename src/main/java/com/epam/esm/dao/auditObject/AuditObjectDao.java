package com.epam.esm.dao.auditObject;

import com.epam.esm.model.audit.AuditObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditObjectDao extends JpaRepository<AuditObject, Long> {
}