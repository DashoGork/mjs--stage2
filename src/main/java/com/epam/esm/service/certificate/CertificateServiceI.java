package com.epam.esm.service.certificate;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.Service;

import java.util.List;

public interface CertificateServiceI extends Service<GiftCertificate> {
    void create(GiftCertificate giftCertificate);
    List<GiftCertificate> read();
    void delete(GiftCertificate giftCertificate);
    void update(Tag tag, GiftCertificate giftCertificate);
    List<GiftCertificate> getAllCertificatesByTagName(String tagName);
    List<GiftCertificate> getByPartOfNameOrDescription(String query);
    List<GiftCertificate> sortByAscDesc(String name, String sortField, String sortOrder);
    List<GiftCertificate> getByTagOrQueryAndSort(String name, String sortField, String sortOrder,
                                                 String tagName);
    GiftCertificate read(long id);

}
