package com.epam.esm.dao.giftCertificate;

import com.epam.esm.model.GiftCertificate;

import java.util.Date;
import java.util.List;

public interface GiftCertificateDao {
    void create(GiftCertificate giftCertificate);
    List<GiftCertificate> read();
    GiftCertificate read(long id);
    void update(GiftCertificate giftCertificate);
    void delete(long id);
    GiftCertificate read(Date date);
}
