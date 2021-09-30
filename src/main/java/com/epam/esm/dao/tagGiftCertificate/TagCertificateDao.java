package com.epam.esm.dao.tagGiftCertificate;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagCertificateDao {
    void add(Tag tag, GiftCertificate certificate);
    List<Integer> readByCertificate(long id);
    List<Integer> readByTag(long id);
    void deleteTag(long id);
    void deleteCertificate(long id);
}
