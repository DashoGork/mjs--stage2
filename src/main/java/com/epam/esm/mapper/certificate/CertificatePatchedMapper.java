package com.epam.esm.mapper.certificate;

import com.epam.esm.model.entity.Certificate;

public interface CertificatePatchedMapper {
    Certificate patchedToCertificate(Certificate patched, Certificate old);
}
