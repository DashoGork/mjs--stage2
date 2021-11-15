package com.epam.esm.dao.giftCertificate;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.entity.Certificate;

import java.util.List;
import java.util.Optional;

public interface CertificateDaoI extends Dao<Certificate> {
    Optional<Certificate> findCertificateByName(String name);

    void patch(Certificate certificate);

    List<Certificate> filterAndSort(String name,
                                    String description,
                                    String sortField,
                                    String sortOrder,
                                    String tagName,
                                    int offset, int limit);
}
