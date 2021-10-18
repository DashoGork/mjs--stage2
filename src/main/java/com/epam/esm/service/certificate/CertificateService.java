package com.epam.esm.service.certificate;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.dao.tagGiftCertificate.TagCertificateDao;
import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
import com.epam.esm.enums.SortOptions;
import com.epam.esm.enums.SortOrder;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.mapper.certificate.CertificateDtoMapper;
import com.epam.esm.mapper.certificate.CertificateDtoMapperImplementation;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.CertificateDto;
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
    private CertificateDtoMapper mapper;

    @Autowired
    public CertificateService(
            GiftCertificateDaoImplementation giftCertificate,
            TagDaoImplementation tagDao,
            TagCertificateDaoImplementation tagCertificateDao,
            CertificateDtoMapperImplementation mapper) {
        this.giftCertificateDao = giftCertificate;
        this.tagDao = tagDao;
        this.tagCertificateDao = tagCertificateDao;
        this.mapper = mapper;
    }

    public CertificateDto create(CertificateDto certificateDto) {
        Date dateOfCreation = new Date();
        Certificate certificate =
                mapper.certificateDtoToCertificate(certificateDto);
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
            }
            return mapper.certificateToCertificateDto(createdCertificate);
        }
        return mapper.certificateToCertificateDto(giftCertificateDao.read(certificate.getName()));
    }

    @Override
    public List<CertificateDto> read() {
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
        List<CertificateDto> dtoCertificates =
                certificates.stream().map((certificate -> mapper.certificateToCertificateDto(certificate))).collect(Collectors.toList());
        return dtoCertificates;
    }

    @Override
    public CertificateDto read(long id) {
        Certificate certificate = giftCertificateDao.read(id);
        List<Long> tagIds;
        List<Tag> tags = new ArrayList<>();
        tagIds = tagCertificateDao.readByCertificate(certificate.getId());
        for (Long tagId : tagIds) {
            tags.add(tagDao.read(tagId));
        }
        certificate.setTags(tags);
        return mapper.certificateToCertificateDto(certificate);
    }

    public void delete(CertificateDto certificate) {
        giftCertificateDao.delete(certificate.getId());
        tagCertificateDao.deleteCertificate(certificate.getId());
    }

    public void patch(long id, CertificateDto patchedCertificate) {
        Certificate oldCertificate = mapper.certificateDtoToCertificate(read(id));
        Certificate certificateFromRequest =
                mapper.certificateDtoToCertificate(patchedCertificate);
        giftCertificateDao.patch(certificateFromRequest, oldCertificate);
        List<Long> allTagsIdOfCertificateFromDb =
                tagCertificateDao.readByCertificate(id);
        if (certificateFromRequest.getTags() != null) {
            for (Tag tag : certificateFromRequest.getTags()) {
                if (!allTagsIdOfCertificateFromDb.contains(tag.getId())) {
                    tagCertificateDao.add(tag, oldCertificate);
                }
            }
        }

    }

    public List<CertificateDto> getAllCertificatesByTagName(String tagName) {
        List<Long> ids = tagCertificateDao.readByTag(tagDao.read(tagName).getId());
        List<Certificate> certificates = new ArrayList<>();
        for (Long id : ids) {
            certificates.add(mapper.certificateDtoToCertificate(read(id)));
        }
        return certificates.stream().map((certificate -> mapper.certificateToCertificateDto(certificate)))
                .collect(Collectors.toList());
    }

    public List<CertificateDto> getByPartOfNameOrDescription(String query) {
        List<Certificate> certificates = searchByPartOfDescription(query);
        certificates.addAll(searchByPartOfName(query));
        certificates = certificates.stream().distinct().collect(Collectors.toList());
        for (Certificate certificate : certificates) {
            certificate.setTags(mapper.certificateDtoToCertificate(read(certificate.getId())).getTags());
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

        return certificates.stream().map((certificate -> mapper.certificateToCertificateDto(certificate))).collect(Collectors.toList());
    }

    public List<CertificateDto> sortByAscDesc(String name, String sortField, String sortOrder) {
        if (name != null & !name.isEmpty()) {
            List<Certificate> listToSort = certificateDtoListToCertificateList(getByPartOfNameOrDescription(name));
            return certificateListToCertificateDtoList(sortByAscDesc(sortField, sortOrder, listToSort));
        } else {
            return certificateListToCertificateDtoList(sortByAscDesc(sortField, sortOrder, certificateDtoListToCertificateList(read())));
        }
    }

    public List<CertificateDto> getByTagOrQueryAndSort(String name,
                                                       String sortField,
                                                       String sortOrder,
                                                       String tagName) {
        List<Certificate> certificates = new ArrayList<>();
        if (!name.isEmpty() & name != null) {
            certificates.addAll(certificateDtoListToCertificateList(getByPartOfNameOrDescription(name)));
        }
        if (!tagName.isEmpty() & tagName != null) {
            certificates.addAll(certificateDtoListToCertificateList(getAllCertificatesByTagName(tagName)));
            certificates = certificates.stream().distinct().collect(Collectors.toList());
        }
        if (certificates.isEmpty()) {
            certificates.addAll(certificateDtoListToCertificateList(read()));
        }
        return certificateListToCertificateDtoList(sortByAscDesc(sortField, sortOrder, certificates));
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
                certificateDtoListToCertificateList(read());
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
                certificateDtoListToCertificateList(read());
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

    private List<Certificate> certificateDtoListToCertificateList(List<CertificateDto> certificateDtoList) {
        return certificateDtoList.stream()
                .map((certificateDto -> mapper.certificateDtoToCertificate(certificateDto)))
                .collect(Collectors.toList());
    }

    private List<CertificateDto> certificateListToCertificateDtoList(List<Certificate> certificateList) {
        return certificateList.stream()
                .map((certificate -> mapper.certificateToCertificateDto(certificate)))
                .collect(Collectors.toList());
    }
}