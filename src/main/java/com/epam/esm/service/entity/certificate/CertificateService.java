package com.epam.esm.service.entity.certificate;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.dao.tagGiftCertificate.TagCertificateDao;
import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
import com.epam.esm.enums.SortOptions;
import com.epam.esm.enums.SortOrder;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CertificateService implements CertificateServiceI {
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private TagCertificateDao tagCertificateDao;


    @Autowired
    public CertificateService(
            GiftCertificateDaoImplementation giftCertificate,
            TagDaoImplementation tagDao,
            TagCertificateDaoImplementation tagCertificateDao
    ) {
        this.giftCertificateDao = giftCertificate;
        this.tagDao = tagDao;
        this.tagCertificateDao = tagCertificateDao;
    }

    public Certificate create(Certificate certificate) {
        Date dateOfCreation = new Date();
        try {
            giftCertificateDao.read(certificate.getName());
        } catch (GiftCertificateNotFoundException e) {
            certificate.setLastUpdateDate(dateOfCreation);
            certificate.setCreateDate(dateOfCreation);
            giftCertificateDao.create(certificate);
            Certificate createdCertificate =
                    giftCertificateDao.read(dateOfCreation);
            createdCertificate.setTags(certificate.getTags());
            for (Tag tag : createdCertificate.getTags()) {
                tagDao.update(tag.getName());
                tagCertificateDao.add(tagDao.read(tag.getName()), createdCertificate);
                tag.setId(tagDao.read(tag.getName()).getId());
            }
            return createdCertificate;
        }
        return giftCertificateDao.read(certificate.getName());
    }

    @Override
    public List<Certificate> read() {
        List<Certificate> certificates = giftCertificateDao.read();
        List<Long> tagIds;
        List<Tag> tagsForEachCertificate = new ArrayList<>();
        for (Certificate certificate : certificates) {
            tagIds = tagCertificateDao.readByCertificate(certificate.getId());
            for (Long id : tagIds) {
                tagsForEachCertificate.add(tagDao.read(id));
            }
            certificate.setTags(tagsForEachCertificate);
            tagsForEachCertificate = new ArrayList<>();
        }
        return certificates;
    }

    @Override
    public Certificate read(long id) {
        Certificate certificate = giftCertificateDao.read(id);
        List<Long> tagIds;
        List<Tag> tags = new ArrayList<>();
        tagIds = tagCertificateDao.readByCertificate(certificate.getId());
        for (Long tagId : tagIds) {
            tags.add(tagDao.read(tagId));
        }
        certificate.setTags(tags);
        return certificate;
    }

    public void delete(Certificate certificate) {
        giftCertificateDao.delete(certificate.getId());
        tagCertificateDao.deleteCertificate(certificate.getId());
    }

    public void patch(long id, Certificate patchedCertificate) {
        Certificate oldCertificate = read(id);
        giftCertificateDao.patch(patchedCertificate, oldCertificate);
        List<Long> allTagsIdOfCertificateFromDb =
                tagCertificateDao.readByCertificate(id);
        if (patchedCertificate.getTags() != null) {
            for (Tag tag : patchedCertificate.getTags()) {
                Tag tagFromDb = tagDao.read(tag.getName());
                if (!allTagsIdOfCertificateFromDb.contains(tagFromDb.getId())) {
                    tagCertificateDao.add(tagFromDb, oldCertificate);
                }
            }
        }
    }

    public List<Certificate> getCertificatesByCriteria(String name,
                                                       String description,
                                                       String sortField,
                                                       String sortOrder,
                                                       String tagName) {
        List<Certificate> certificates = new ArrayList<>();
        if (!name.isEmpty() | !description.isEmpty() | !tagName.isEmpty()) {
            certificates.addAll((searchByPartOfName(name)));
            certificates.addAll((searchByPartOfDescription(description)));
            certificates.addAll((getAllCertificatesByTagName(tagName)));
        } else {
            certificates.addAll((read()));
        }
        return (sortByAscDesc(sortField, sortOrder, certificates.stream().distinct().collect(Collectors.toList())));
    }

    private List<Certificate> sortByAscDesc(String sortField, String sortOrder, List<Certificate> listToSort) {
        if (!sortField.isEmpty() & !sortOrder.isEmpty()) {
            Comparator<Certificate> comparator;
            if (SortOptions.DATE.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                comparator = Comparator.comparing(certificate -> certificate.getCreateDate());
            } else if (SortOptions.NAME.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                comparator = Comparator.comparing(certificate -> certificate.getName());
            } else {
                throw new IllegalArgumentException("Invalid sort field " + sortField);
            }
            if (SortOrder.ASC.name().equals(sortOrder.toUpperCase(Locale.ROOT)) | SortOrder.DESC
                    .name().equals(sortOrder.toUpperCase(Locale.ROOT))) {
                int sortOrderType =
                        SortOrder.valueOf(sortOrder.toUpperCase(Locale.ROOT)).getValue();
                listToSort = listToSort.stream().sorted((o1, o2) -> comparator.compare(o1, o2) *
                        sortOrderType).collect(Collectors.toList());
            } else {
                throw new IllegalArgumentException("Invalid sort order " + sortOrder);
            }
        }
        return listToSort;
    }

    List<Certificate> getAllCertificatesByTagName(String tagName) {
        List<Long> ids = tagCertificateDao.readByTag(tagDao.read(tagName).getId());
        List<Certificate> certificates = new ArrayList<>();
        for (Long id : ids) {
            certificates.add(read(id));
        }
        return certificates;
    }

    List<Certificate> searchByPartOfName(String query) {
        log.info("Search giftCertificates by part of name with query = " + query);
        List<Certificate> listOfAll =
                read();
        List<Certificate> sortedList = new ArrayList<>();
        for (String substring : query.split(" ")) {
            sortedList.addAll(listOfAll.stream()
                    .filter((giftCertificate ->
                            giftCertificate.getName().toLowerCase().contains(substring.toLowerCase())))
                    .collect(Collectors.toList()));
        }
        return sortedList;
    }

    List<Certificate> searchByPartOfDescription(String query) {
        log.info("Search giftCertificates by part of description with query = " + query);
        List<Certificate> listOfAll =
                read();
        List<Certificate> sortedList = new ArrayList<>();
        for (String substring : query.split(" ")) {
            sortedList.addAll(listOfAll.stream()
                    .filter((giftCertificate ->
                            giftCertificate.getDescription().toLowerCase().contains(substring.toLowerCase())))
                    .collect(Collectors.toList()));
        }
        return sortedList;
    }
}