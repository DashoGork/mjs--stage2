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

    public List<Certificate> getAllCertificatesByTagName(String tagName) {
        List<Long> ids = tagCertificateDao.readByTag(tagDao.read(tagName).getId());
        List<Certificate> certificates = new ArrayList<>();
        for (Long id : ids) {
            certificates.add(read(id));
        }
        return certificates;
    }

    public List<Certificate> getByPartOfNameOrDescription(String query) {
        List<Certificate> certificates = searchByPartOfDescription(query);
        certificates.addAll(searchByPartOfName(query));
        certificates = certificates.stream().distinct().collect(Collectors.toList());
        for (Certificate certificate : certificates) {
            certificate.setTags(read(certificate.getId()).getTags());
        }
        certificates = certificates.stream().sorted(new Comparator<Certificate>() {
            @Override
            public int compare(Certificate o1, Certificate o2) {
                return (getNumberOfWordsInjections(query.split(" "), o2.getDescription())
                        - getNumberOfWordsInjections(query.split(" "), o1.getDescription())
                        + getNumberOfWordsInjections(query.split(" "), o2.getName())
                        - getNumberOfWordsInjections(query.split(" "), o1.getName())
                );
            }
        }).collect(Collectors.toList());

        return certificates;
    }

    public List<Certificate> sortByAscDesc(String name, String sortField, String sortOrder) {
        if (name != null & !name.isEmpty()) {
            List<Certificate> listToSort = (getByPartOfNameOrDescription(name));
            return (sortByAscDesc(sortField, sortOrder, listToSort));
        } else {
            return (sortByAscDesc(sortField, sortOrder, (read())));
        }
    }

    public List<Certificate> getByTagOrQueryAndSort(String name,
                                                    String sortField,
                                                    String sortOrder,
                                                    String tagName) {
        List<Certificate> certificates = new ArrayList<>();
        if (!name.isEmpty() & name != null) {
            certificates.addAll((getByPartOfNameOrDescription(name)));
            certificates = certificates.stream().distinct().collect(Collectors.toList());
        }
        if (!tagName.isEmpty() & tagName != null) {
            certificates.addAll((getAllCertificatesByTagName(tagName)));
            certificates = certificates.stream().distinct().collect(Collectors.toList());
        }
        if (certificates.isEmpty()) {
            certificates.addAll((read()));
        }
        return (sortByAscDesc(sortField, sortOrder, certificates));
    }

    private List<Certificate> sortByAscDesc(String sortField, String sortOrder, List<Certificate> listToSort) {
        if (sortField != null & !sortField.isEmpty() & !sortOrder.isEmpty() & sortOrder != null) {
            Comparator<Certificate> nameComparator = Comparator.comparing(certificate -> certificate.getName());
            Comparator<Certificate> dateComparator = Comparator.comparing(certificate -> certificate.getCreateDate());
            Comparator comparator;
            if (SortOptions.DATE.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                comparator = dateComparator;
            } else if (SortOptions.NAME.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                comparator = nameComparator;
            } else {
                throw new IllegalArgumentException("Invalid sort field " + sortField);
            }
            listToSort = listToSort.stream().sorted(new Comparator<Certificate>() {
                @Override
                public int compare(Certificate o1, Certificate o2) {
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
        sortedList = sortedList.stream().distinct().collect(Collectors.toList());
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
        sortedList = sortedList.stream().distinct().collect(Collectors.toList());
        return sortedList;
    }
}