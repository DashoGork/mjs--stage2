package com.epam.esm.service.entity.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.service.entity.Service;

import java.util.List;

public interface CertificateServiceI extends Service<Certificate> {
    void patch(long id, Certificate patchedCertificate);

    List<Certificate> getAllCertificatesByTagName(String tagName);

    List<Certificate> getByPartOfNameOrDescription(String query);

    List<Certificate> sortByAscDesc(String name, String sortField, String sortOrder);

    List<Certificate> getByTagOrQueryAndSort(String name, String sortField, String sortOrder,
                                             String tagName);
}