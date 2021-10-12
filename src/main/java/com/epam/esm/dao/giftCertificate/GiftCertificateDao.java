package com.epam.esm.dao.giftCertificate;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.GiftCertificate;

import java.util.Date;

public interface GiftCertificateDao extends Dao<GiftCertificate> {
    GiftCertificate read(Date date);
    void patch(GiftCertificate patchedCertificate,
               GiftCertificate oldCertificate);
    void update(GiftCertificate giftCertificate);
}