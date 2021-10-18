package com.epam.esm.mapper.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.dto.CertificateDto;
import org.mapstruct.Mapper;

@Mapper
public interface CertificateDtoMapper {
    Certificate certificateDtoToCertificate(CertificateDto certificateDto);

    CertificateDto certificateToCertificateDto(Certificate certificate);
}
