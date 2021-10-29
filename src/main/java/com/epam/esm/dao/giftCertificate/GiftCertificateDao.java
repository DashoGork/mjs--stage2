package com.epam.esm.dao.giftCertificate;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.entity.Certificate;

import java.util.Date;

public interface GiftCertificateDao extends Dao<Certificate> {
    Certificate read(Date date);
    void patch(Certificate patchedCertificate,
               Certificate oldCertificate);
    void update(Certificate certificate);
    Certificate read(String name);
}