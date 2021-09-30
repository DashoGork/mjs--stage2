package com.epam.esm.service;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.dao.tagGiftCertificate.TagCertificateDao;
import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CertificateService implements Service<GiftCertificate> {
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private TagCertificateDao tagCertificateDao;

    @Autowired
    public CertificateService(GiftCertificateDaoImplementation giftCertificate, TagDaoImplementation tagDao, TagCertificateDaoImplementation tagCertificateDao) {
        this.giftCertificateDao = giftCertificate;
        this.tagDao = tagDao;
        this.tagCertificateDao=tagCertificateDao;
    }



   @Override
    public void create(GiftCertificate giftCertificate) {
        giftCertificateDao.create(giftCertificate);
        //tagCertificateDao.add(tag,giftCertificate);
        //tagDao.update(tag.getName());
    }


}
