package com.epam.esm.service.dto.certificate;

import com.epam.esm.mapper.certificate.CertificateDtoMapper;
import com.epam.esm.mapper.certificate.CertificateDtoMapperImplementation;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.service.entity.certificate.CertificateService;
import com.epam.esm.service.entity.certificate.CertificateServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateDtoService implements CertificateDtoServiceI {

    private CertificateDtoMapper mapper;
    private CertificateServiceI service;

    @Autowired
    public CertificateDtoService(CertificateDtoMapperImplementation mapper,
                                 CertificateService service) {
        this.mapper = mapper;
        this.service = service;
    }


    @Override
    public CertificateDto create(CertificateDto certificateDto) {
        Certificate certificate = service.create(mapper.certificateDtoToCertificate(certificateDto));
        return mapper.certificateToCertificateDto(certificate);
    }

    @Override
    public void delete(CertificateDto certificateDto) {
        service.delete(mapper.certificateDtoToCertificate(certificateDto));
    }

    @Override
    public List<CertificateDto> read() {
        return certificateListToCertificateDtoList(service.read());
    }

    @Override
    public CertificateDto read(long id) {
        return mapper.certificateToCertificateDto(service.read(id));
    }

    @Override
    public void patch(long id, CertificateDto patchedCertificate) {
        service.patch(id, mapper.certificateDtoToCertificate(patchedCertificate));
    }

    @Override
    public List<CertificateDto> getByTagOrQueryAndSort(String name,
                                                       String description,
                                                       String sortField, String sortOrder, String tagName) {
        return certificateListToCertificateDtoList(service.getCertificatesByCriteria(name, description
                , sortField, sortOrder, tagName));
    }

    private List<CertificateDto> certificateListToCertificateDtoList(List<Certificate> certificateList) {
        return certificateList.stream()
                .map((certificate -> mapper.certificateToCertificateDto(certificate)))
                .collect(Collectors.toList());
    }

    @Override
    public List<CertificateDto> findPaginated(String name, String description,
                                              String sortField, String sortOrder,
                                              String tagName, int page, int size) {
        List<CertificateDto> certificates = getByTagOrQueryAndSort(name,
                description, sortField, sortOrder, tagName);
        List<CertificateDto> paginatedCertificates = new ArrayList<>();
        try {
            paginatedCertificates =
                    certificates.subList(page * size, page * size + size);
        } catch (IndexOutOfBoundsException e) {
        }
        return paginatedCertificates;
    }
}
