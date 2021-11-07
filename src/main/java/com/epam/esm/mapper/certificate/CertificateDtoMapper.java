package com.epam.esm.mapper.certificate;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.entity.Certificate;
import org.mapstruct.Mapper;

@Mapper
public interface CertificateDtoMapper {
    Certificate certificateDtoToCertificate(CertificateDto certificateDto);

    CertificateDto certificateToCertificateDto(Certificate certificate);
}
