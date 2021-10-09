package com.epam.esm.service.certificate;

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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CertificateService implements CertificateServiceI {
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


    public void create(GiftCertificate giftCertificate, Tag... tags) {
        giftCertificateDao.create(giftCertificate);
        for (Tag tag : tags) {
            tagDao.update(tag.getName());
            tagCertificateDao.add(tag, giftCertificate);
        }
    }

    @Override
    public List<GiftCertificate> read() {
        List<GiftCertificate> giftCertificates = giftCertificateDao.read();
        List<Long> tagIds;
        List<Tag> tagsForEachCertificate = new ArrayList<>();
        for (GiftCertificate giftCertificate : giftCertificates) {
            tagIds = tagCertificateDao.readByCertificate(giftCertificate.getId());
            for (Long id : tagIds) {
                tagsForEachCertificate.add(tagDao.read(id));
            }
            giftCertificate.setTags(tagsForEachCertificate);
            tagsForEachCertificate = new ArrayList<>();
        }
        return giftCertificates;
    }

    @Override
    public GiftCertificate read(long id) {
        GiftCertificate giftCertificate = giftCertificateDao.read(id);
        List<Long> tagIds;
        List<Tag> tags = new ArrayList<>();
        tagIds = tagCertificateDao.readByCertificate(giftCertificate.getId());
        for (Long tagId : tagIds) {
            tags.add(tagDao.read(tagId));
        }
        giftCertificate.setTags(tags);
        return giftCertificate;
    }

    public void delete(GiftCertificate giftCertificate) {
        giftCertificateDao.delete(giftCertificate.getId());
        tagCertificateDao.deleteCertificate(giftCertificate.getId());
    }

    public void update(Tag tag, GiftCertificate giftCertificate) {

        giftCertificateDao.update(giftCertificate);
    }

    public List<GiftCertificate> getAllCertificatesByTagName(String tagName) {
        List<Long> ids = tagCertificateDao.readByTag(tagDao.read(tagName).getId());
        List<GiftCertificate> certificates = new ArrayList<>();
        for (Long id : ids) {
            certificates.add(read(id));
        }
        return certificates;
    }

    public List<GiftCertificate> getByPartOfNameOrDescription(String query) {
        List<GiftCertificate> certificates = giftCertificateDao.searchByPartOfDescription(query);
        certificates.addAll(giftCertificateDao.searchByPartOfName(query));
        certificates = certificates.stream().distinct().collect(Collectors.toList());
        for (GiftCertificate certificate : certificates) {
            certificate.setTags(read(certificate.getId()).getTags());
        }
        certificates = certificates.stream().sorted(new Comparator<GiftCertificate>() {
            @Override
            public int compare(GiftCertificate o1, GiftCertificate o2) {
                return (getNumberOfWordsInjections(query.split(" "), o2.getDescription())
                        - getNumberOfWordsInjections(query.split(" "), o1.getDescription())
                        + getNumberOfWordsInjections(query.split(" "), o2.getName())
                        - getNumberOfWordsInjections(query.split(" "), o1.getName())
                );
            }
        }).collect(Collectors.toList());
        return certificates;
    }

    public List<GiftCertificate> sortByAscDesc(String name, String sortField, String sortOrder) {
        if (name != null & !name.equals("default")) {
            List<GiftCertificate> listToSort = getByPartOfNameOrDescription(name);
            return sortByAscDesc(sortField, sortOrder, listToSort);
        } else {
            return sortByAscDesc(sortField, sortOrder, read());
        }
    }

    private List<GiftCertificate> sortByAscDesc(String sortField, String sortOrder, List<GiftCertificate> listToSort) {
        if (sortField != null & sortOrder != null) {
            Comparator<GiftCertificate> nameComparator = Comparator.comparing(certificate -> certificate.getName());
            Comparator<GiftCertificate> dateComparator = Comparator.comparing(certificate -> certificate.getCreateDate());
            Comparator comparator;
            if (SortOptions.DATE.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                comparator = dateComparator;
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

    }

    private int getNumberOfWordsInjections(String[] searchWords, String searchContainer) {
        return Arrays.stream(searchWords)
                .reduce(0, (a, b) -> (searchContainer.contains(b) ? 1 : 0) + a, Integer::sum);
    }

}
