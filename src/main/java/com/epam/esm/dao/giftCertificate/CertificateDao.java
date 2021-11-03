package com.epam.esm.dao.giftCertificate;

import com.epam.esm.model.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateDao extends JpaRepository<Certificate, Long> {
    Certificate findCertificateByName(String name);
}
