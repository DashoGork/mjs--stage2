package com.epam.esm.service;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.dao.tagGiftCertificate.TagCertificateDao;
import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
import com.epam.esm.enums.SortOptions;
import com.epam.esm.enums.SortOrder;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CertificateService implements Service<GiftCertificate> {
    private GiftCertificateDao giftCertificateDao;
    private TagDao tagDao;
    private TagCertificateDao tagCertificateDao;

    @Autowired
    public CertificateService(
            GiftCertificateDaoImplementation giftCertificate,
            TagDaoImplementation tagDao,
            TagCertificateDaoImplementation tagCertificateDao) {
        this.giftCertificateDao = giftCertificate;
        this.tagDao = tagDao;
        this.tagCertificateDao = tagCertificateDao;
    }


    public void create(GiftCertificate giftCertificate, Tag tag) {
        giftCertificateDao.create(giftCertificate);
        tagCertificateDao.add(tag, giftCertificate);
        tagDao.update(tag.getName());
    }

    public List<GiftCertificate> read() {
        return giftCertificateDao.read();
    }

    public void delete(GiftCertificate giftCertificate) {
        giftCertificateDao.delete(giftCertificate.getId());
        tagCertificateDao.deleteCertificate(giftCertificate.getId());
    }

    public void update() {
        ///
    }

    public List<GiftCertificate> getAllCertificatesByTag(Tag tag) {
        List<Integer> ids = tagCertificateDao.readByTag(tagDao.read(tag.getName()).getId());
        List<GiftCertificate> certificates = new ArrayList<>();
        for (Integer id : ids) {
            certificates.add(giftCertificateDao.read(id));
        }
        return certificates;
    }

    public List<GiftCertificate> getByPartOfNameOrDescription(String query) {
        List<GiftCertificate> certificates = giftCertificateDao.searchByPartOfDescription(query);
        certificates.addAll(giftCertificateDao.searchByPartOfName(query));
        certificates = certificates.stream().distinct().collect(Collectors.toList());
        certificates = certificates.stream().sorted(new Comparator<GiftCertificate>() {
            @Override
            public int compare(GiftCertificate o1, GiftCertificate o2) {
                return (getNumberOfWordsInjections(query.split(" "), o2.getDescription())
                        - getNumberOfWordsInjections(query.split(" "), o1.getDescription())
                        + getNumberOfWordsInjections(query.split(" "), o2.getName())
                        - getNumberOfWordsInjections(query.split(" "), o1.getName()));
            }
        }).collect(Collectors.toList());
        return certificates;
    }

    public List<GiftCertificate> SortByAscDesc(String name, String sortField, String sortOrder) {
        if (name != null) {
            List<GiftCertificate> listToSort = getByPartOfNameOrDescription(name);
            if (sortField != null & sortOrder != null) {
                Comparator<GiftCertificate> nameComparator = Comparator.comparing(certificate -> certificate.getName());
                Comparator<GiftCertificate> descriptionComparator = Comparator.comparing(certificate -> certificate.getDescription());
                Comparator comparator;
                if (SortOptions.DESCRIPTION.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                    comparator = descriptionComparator;
                } else if (SortOptions.NAME.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                    comparator = nameComparator;
                } else {
                    throw new IllegalArgumentException("Invalid sort field " + sortField);
                }
                listToSort = listToSort.stream().sorted(new Comparator<GiftCertificate>() {
                    @Override
                    public int compare(GiftCertificate o1, GiftCertificate o2) {
                        return comparator.compare(o1, o2) * SortOrder.valueOf(sortOrder.toUpperCase(Locale.ROOT)).getValue();
                    }
                }).collect(Collectors.toList());
            }
            return listToSort;
        } else {
            return read();
        }
    }

    private int getNumberOfWordsInjections(String[] searchWords, String searchContainer) {
        return Arrays.stream(searchWords).reduce(0, (a, b) -> (searchContainer.contains(b) ? 1 : 0) + a, Integer::sum);
    }

}
