package com.epam.esm.service.entity.certificate;

import com.epam.esm.dao.giftCertificate.CertificateDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.enums.SortOptions;
import com.epam.esm.enums.SortOrder;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.mapper.certificate.CertificatePatchedMapper;
import com.epam.esm.mapper.certificate.CertificatePatchedMapperImpl;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.PaginationService;
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
public class CertificateService implements CertificateServiceI, PaginationService {
    private TagDao tagDao;
    private CertificateDao certificateDao;
    private TagServiceI tagService;
    private CertificatePatchedMapper patchedMapper;


    @Autowired
    public CertificateService(
            TagDao tagDao,
            CertificateDao certificateDao,
            TagService tagService,
            CertificatePatchedMapperImpl patchedMapper
    ) {
        this.tagDao = tagDao;
        this.certificateDao = certificateDao;
        this.tagService = tagService;
        this.patchedMapper = patchedMapper;
    }

    Certificate read(String name) {
        if (name != null) {
            Optional<Certificate> certificate =
                    Optional.ofNullable(certificateDao.findCertificateByName(name));
            if (!certificate.isPresent()) {
                throw new GiftCertificateNotFoundException("Certificate " +
                        "wasn't" +
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
            return certificateDao.save(certificate);
        }
        return read(certificate.getName());
    }

    @Override
    public List<Certificate> read() {
        return certificateDao.findAll();
    }

    @Override
    public Certificate read(long id) {
        Optional<Certificate> certificate = certificateDao.findById(id);
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
        certificateDao.delete(certificate);
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
        certificateDao.saveAndFlush(patchedMapper.patchedToCertificate(patchedCertificate, oldCertificate));
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

    @Override
    public List<Certificate> findPaginated(String name, String description, String sortField, String sortOrder, String tagName, int page, int size) {
        List<Certificate> certificates = getCertificatesByCriteria(name,
                description, sortField, sortOrder, tagName);
        return paginate(certificates, size, page);
    }

    List<Certificate> sortByAscDesc(String sortField, String sortOrder, List<Certificate> listToSort) {
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
        List<Certificate> certificates = new ArrayList<>();
        if (!tagName.isEmpty()) {
            String[] tagsNames = tagName.split(",");
            Set<Tag> tags = new HashSet<>();
            for (String name : tagsNames) {
                Tag tag = tagDao.findTagByName(name.trim());
                if (tag != null) {
                    tags.add(tag);
                }
            }
            certificates = certificateDao.findAll();
            certificates =
                    certificates.stream().filter((certificate -> certificate.getTags().containsAll(tags))).collect(Collectors.toList());
        }
        return certificates;
    }

    List<Certificate> searchByPartOfName(String query) {
        log.info("Search giftCertificates by part of name with query = " + query);
        List<Certificate> sortedList = new ArrayList<>();
        if (!query.isEmpty()) {
            List<Certificate> listOfAll =
                    read();
            for (String substring : query.split(" ")) {
                sortedList.addAll(listOfAll.stream()
                        .filter((giftCertificate ->
                                giftCertificate.getName().toLowerCase().contains(substring.toLowerCase())))
                        .collect(Collectors.toList()));
            }
        }
        return sortedList;
    }

    List<Certificate> searchByPartOfDescription(String query) {
        log.info("Search giftCertificates by part of description with query = " + query);
        List<Certificate> sortedList = new ArrayList<>();
        if (!query.isEmpty()) {
            List<Certificate> listOfAll =
                    read();
            for (String substring : query.split(" ")) {
                sortedList.addAll(listOfAll.stream()
                        .filter((giftCertificate ->
                                giftCertificate.getDescription().toLowerCase().contains(substring.toLowerCase())))
                        .collect(Collectors.toList()));
            }
        }
        return sortedList;
    }
}