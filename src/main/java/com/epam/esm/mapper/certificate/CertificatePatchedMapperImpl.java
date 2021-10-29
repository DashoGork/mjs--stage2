package com.epam.esm.mapper.certificate;

import com.epam.esm.model.entity.Certificate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CertificatePatchedMapperImpl implements CertificatePatchedMapper {
    @Override
    public Certificate patchedToCertificate(Certificate patched, Certificate old) {
        Certificate certificateToReturn = new Certificate();
        certificateToReturn.setId(patched.getId());
        if (patched.getName() != null) {
            old.setName(patched.getName());
        }
        if (patched.getDescription() != null) {
            old.setDescription(patched.getDescription());
        }
        if (patched.getDuration() != 0) {
            old.setDuration(patched.getDuration());
        }
        if (patched.getPrice() != 0) {
            old.setPrice(patched.getPrice());
        }
        old.setLastUpdateDate(new Date());
        return old;
    }
}
