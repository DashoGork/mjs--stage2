package com.epam.esm.service.dto.certificate;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.entity.Service;

import java.util.List;

public interface CertificateDtoServiceI extends Service<CertificateDto> {
    void patch(long id, CertificateDto patchedCertificate);

    List<CertificateDto> getByTagOrQueryAndSort(String name, String description,
                                                String sortField, String sortOrder,
                                                String tagName);

    List<CertificateDto> findPaginated(String name, String description,
                                       String sortField, String sortOrder,
                                       String tagName,
                                       int page, int size);
}
