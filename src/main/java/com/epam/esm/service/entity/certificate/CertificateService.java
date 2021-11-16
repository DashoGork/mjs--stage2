package com.epam.esm.service.entity.certificate;

import com.epam.esm.dao.giftCertificate.CertificateDaoI;
import com.epam.esm.dao.giftCertificate.impl.CertificateDao;
import com.epam.esm.dao.tag.TagDaoI;
import com.epam.esm.dao.tag.impl.TagDao;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.mapper.certificate.CertificatePatchedMapper;
import com.epam.esm.mapper.certificate.CertificatePatchedMapperImpl;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.PaginationCalcService;
import com.epam.esm.service.entity.tag.TagService;
import com.epam.esm.service.entity.tag.TagServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CertificateService implements CertificateServiceI, PaginationCalcService {
    private TagDaoI tagDao;
    private CertificateDaoI certificateDaoI;
    private TagServiceI tagService;
    private CertificatePatchedMapper patchedMapper;


    @Autowired
    public CertificateService(
            TagDao tagDao,
            TagService tagService,
            CertificatePatchedMapperImpl patchedMapper,
            CertificateDao certificateDaoI
    ) {
        this.certificateDaoI = certificateDaoI;
        this.tagDao = tagDao;
        this.tagService = tagService;
        this.patchedMapper = patchedMapper;
    }

    Certificate read(String name) {
        if (name != null) {
            Optional<Certificate> certificate =
                    (certificateDaoI.findCertificateByName(name));
            if (!certificate.isPresent()) {
                throw new GiftCertificateNotFoundException("Certificate wasn't" +
                        " found. name =" + name);
            } else {
                return certificate.get();
            }
        } else {
            throw new InvalidParameterException("name is null");
        }
    }

    @Transactional
    public Certificate create(Certificate certificate) {
        Date dateOfCreation = new Date();
        try {
            read(certificate.getName());
        } catch (GiftCertificateNotFoundException e) {
            certificate.setLastUpdateDate(dateOfCreation);
            certificate.setCreateDate(dateOfCreation);
            Set<Tag> tags = certificate.getTags().stream()
                    .map((tag -> tagService.create(tag))).collect(Collectors.toSet());
            certificate.setTags(tags);
            certificateDaoI.create(certificate);
            return read(certificate.getName());
        }
        return read(certificate.getName());
    }

    @Override
    public List<Certificate> read() {
        return certificateDaoI.read();
    }

    @Override
    public Certificate read(long id) {
        Optional<Certificate> certificate = certificateDaoI.read(id);
        if (!certificate.isPresent()) {
            throw new GiftCertificateNotFoundException("Certificate " +
                    "wasn't" +
                    " found. id =" + id);
        } else {
            return certificate.get();
        }
    }

    @Transactional
    public void delete(Certificate certificate) {
        certificateDaoI.delete(certificate);
    }

    @Transactional
    public void patch(long id, Certificate patchedCertificate) {
        Certificate oldCertificate = read(id);
        List<Tag> oldTags = tagDao.findTagsByCertificates(oldCertificate);
        if (patchedCertificate.getTags() != null) {
            for (Tag tag : patchedCertificate.getTags()) {
                tag = tagService.create(tag);
                if (!oldTags.contains(tag)) {
                    oldCertificate.addTag(tag);
                }
            }
        }
        certificateDaoI.patch(patchedMapper.patchedToCertificate(patchedCertificate, oldCertificate));
    }

    @Override
    public List<Certificate> findPaginated(String name, String description, String sortField, String sortOrder, String tagName, int page, int size) {
        Map<String, Integer> indexes = paginate(read().size(), size, page);
        return certificateDaoI.filterAndSort(name, description,
                sortField, sortOrder, tagName, indexes.get("offset"), indexes.get("limit"));
    }
}