package com.epam.esm.service.entity.certificate;

import com.epam.esm.dao.giftCertificate.impl.CertificateDao;
import com.epam.esm.dao.tag.impl.TagDao;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.mapper.certificate.CertificateDtoMapperImplementation;
import com.epam.esm.mapper.certificate.CertificatePatchedMapperImpl;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.tag.TagService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private TagDao tagDao;
    @Mock
    private CertificateDao certificateDao;
    @Mock
    private TagService tagService;
    @Mock
    private CertificatePatchedMapperImpl patchedMapper;
    private CertificateService service;
    private Tag tag;
    private Certificate certificate;
    private List<Certificate> expectedList;
    private CertificateDtoMapperImplementation mapper;
    private Certificate secondCertificate;

    @Before
    public void setUp() throws Exception {
        service = new CertificateService(tagDao, tagService,
                patchedMapper, certificateDao);
        tag = new Tag();
        tag.setName("name");
        tag.setId(1);
        certificate = new Certificate();
        certificate.setName("name");
        certificate.setDescription("desc");
        certificate.setDuration(2);
        certificate.setPrice(12);
        certificate.setCreateDate(new Date());
        certificate.setLastUpdateDate(new Date());
        expectedList = new ArrayList<>();
        expectedList.add(certificate);
        secondCertificate = new Certificate();
        secondCertificate.setName("name2");
        secondCertificate.setDescription("desc");
        secondCertificate.setDuration(2);
        secondCertificate.setPrice(9);
        secondCertificate.setCreateDate(new Date());
        secondCertificate.setLastUpdateDate(new Date());

    }


    @Test
    public void createAlreadyExisting() {
        when(certificateDao.findCertificateByName("name")).thenReturn(Optional.of(certificate));
        Certificate actualCertificate = service.create((certificate));
        assertTrue(actualCertificate.equals(certificate));
    }

    @Test
    public void create() {
        when(certificateDao.findCertificateByName("name")).thenReturn(Optional.of(certificate));
        when(tagService.create(tag)).thenReturn(tag);
        doNothing().when(certificateDao).create(certificate);
        Certificate actualCertificate = service.create((certificate));
        assertTrue(actualCertificate.equals(certificate));
    }

    @Test
    public void read() {
        List<Certificate> expected = new ArrayList<>();
        expected.add(certificate);
        when(certificateDao.read()).thenReturn(expected);
        List<Certificate> actualList = service.read();
        assertTrue(actualList.equals(expected));
    }

    @Test
    public void readByNotExistingName() {
        when(certificateDao.findCertificateByName("")).thenReturn(Optional.ofNullable(null));
        expectedException.expect(GiftCertificateNotFoundException.class);
        Certificate c = service.read("");
    }

    @Test
    public void testRead() {
        Optional<Certificate> expected = Optional.ofNullable(certificate);
        when(certificateDao.read(1l)).thenReturn(expected);
        Certificate actualCertificate = service.read(1);
        assertTrue(actualCertificate.getDescription().equals(certificate.getDescription()));
        assertTrue(actualCertificate.getName().equals(certificate.getName()));
    }

    @Test
    public void testReadNotExisting() {
        Optional<Certificate> expected = Optional.ofNullable(null);
        when(certificateDao.read(1l)).thenReturn(expected);
        expectedException.expect(GiftCertificateNotFoundException.class);
        service.read(1);
    }

    @Test
    public void delete() {
        doNothing().when(certificateDao).delete(certificate);
        service.delete((certificate));
        verify(certificateDao).delete(certificate);
    }

    @Test
    public void patch() {
        Optional<Certificate> expected = Optional.ofNullable(certificate);
        Certificate patchedCertificate = new Certificate();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(tag);
        patchedCertificate.setTags(tagSet);
        patchedCertificate.setPrice(300);
        when(certificateDao.read(1l)).thenReturn(expected);
        when(tagService.create(tag)).thenReturn(tag);
        doNothing().when(certificateDao).create(certificate);
        when(tagDao.findTagsByCertificates(certificate)).thenReturn(tags);
        service.patch(1, patchedCertificate);
        verify(certificateDao).patch(any());
    }

    @Test
    public void findPaginated() {
        when(certificateDao.read()).thenReturn(expectedList);
        when(certificateDao.filterAndSort("", "", "", "", "", 0, 1)).thenReturn(expectedList);
        assertTrue(service.findPaginated("", "", "", "", "", 1, 2).size() == 1);
    }

}