package com.epam.esm.service.entity.certificate;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.service.entity.Service;

import java.util.List;

public interface CertificateServiceI extends Service<Certificate> {
    void patch(long id, Certificate patchedCertificate);

    List<Certificate> getCertificatesByCriteria(String name, String description,
                                                String sortField, String sortOrder,
                                                String tagName);


}