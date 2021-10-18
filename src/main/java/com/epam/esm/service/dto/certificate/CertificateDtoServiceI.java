package com.epam.esm.service.dto.certificate;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.entity.Service;

import java.util.List;

public interface CertificateDtoServiceI extends Service<CertificateDto> {
    void patch(long id, CertificateDto patchedCertificate);

    List<CertificateDto> getAllCertificatesByTagName(String tagName);

    List<CertificateDto> getByPartOfNameOrDescription(String query);

    List<CertificateDto> sortByAscDesc(String name, String sortField, String sortOrder);

    List<CertificateDto> getByTagOrQueryAndSort(String name, String sortField, String sortOrder,
                                                String tagName);
}
