package com.epam.esm.dao.tagGiftCertificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagCertificateDao {
    void add(Tag tag, Certificate certificate);

    List<Long> readByCertificate(long id);
    List<Long> readByTag(long id);
    void deleteTag(long id);
    void deleteCertificate(long id);
}