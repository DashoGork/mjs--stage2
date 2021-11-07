package com.epam.esm.service.entity.certificate;

import com.epam.esm.dao.giftCertificate.CertificateDao;
import com.epam.esm.dao.tag.TagDao;
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

import java.security.InvalidParameterException;
import java.security.Security;
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
        service = new CertificateService(tagDao, certificateDao, tagService, patchedMapper);
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
        when(certificateDao.findCertificateByName("name")).thenReturn(certificate);
        Certificate actualCertificate = service.create((certificate));
        assertTrue(actualCertificate.equals(certificate));
    }

    @Test
    public void create() {
        when(certificateDao.findCertificateByName("name")).thenReturn(null);
        when(tagService.create(tag)).thenReturn(tag);
        when(certificateDao.save(certificate)).thenReturn(certificate);
        Certificate actualCertificate = service.create((certificate));
        assertTrue(actualCertificate.equals(certificate));
    }

    @Test
    public void read() {
        List<Certificate> expected = new ArrayList<>();
        expected.add(certificate);
        when(certificateDao.findAll()).thenReturn(expected);
        List<Certificate> actualList = service.read();
        assertTrue(actualList.equals(expected));
    }

    @Test
    public void readByNotExistingName() {
        when(certificateDao.findCertificateByName("")).thenReturn(null);
        expectedException.expect(GiftCertificateNotFoundException.class);
        Certificate c = service.read("");
    }

    @Test
    public void testRead() {
        Optional<Certificate> expected = Optional.ofNullable(certificate);
        when(certificateDao.findById(1l)).thenReturn(expected);
        Certificate actualCertificate = service.read(1);
        assertTrue(actualCertificate.getDescription().equals(certificate.getDescription()));
        assertTrue(actualCertificate.getName().equals(certificate.getName()));
    }

    @Test
    public void testReadNotExisting() {
        Optional<Certificate> expected = Optional.ofNullable(null);
        when(certificateDao.findById(1l)).thenReturn(expected);
        expectedException.expect(GiftCertificateNotFoundException.class);
        service.read(1);
    }

    @Test
    public void testReadNotValid() {
        expectedException.expect(InvalidParameterException.class);
        service.read(0);
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
        when(certificateDao.findById(1l)).thenReturn(expected);
        when(tagService.create(tag)).thenReturn(tag);
        when((certificateDao).saveAndFlush(any())).thenReturn(certificate);
        when(tagDao.findTagsByCertificates(certificate)).thenReturn(tags);
        service.patch(1, patchedCertificate);
        verify(certificateDao).saveAndFlush(any());
    }

    @Test
    public void getCertificatesByCriteriaEmpty() {
        when(certificateDao.findAll()).thenReturn(expectedList);
        assertTrue(service.getCertificatesByCriteria("", "", "", "", "").equals(expectedList));
    }

    @Test
    public void getCertificatesByCriteriaName() {
        when(certificateDao.findAll()).thenReturn(expectedList);
        assertTrue(service.getCertificatesByCriteria("name", "", "", "", "").equals(expectedList));
    }

    @Test
    public void getCertificatesByCriteriaNameNonExisting() {
        when(certificateDao.findAll()).thenReturn(expectedList);
        assertTrue(service.getCertificatesByCriteria("fg", "", "", "", "").size() == 0);
    }

    @Test
    public void findPaginated() {
        when(certificateDao.findAll()).thenReturn(expectedList);
        assertTrue(service.findPaginated("name", "", "", "", "", 1, 1).size() == 1);

    }

    @Test
    public void sortByAscDescByName() {
        expectedList.add(secondCertificate);
        assertTrue(service.sortByAscDesc("name", "asc", expectedList).get(0).equals(certificate));
    }

    @Test
    public void sortByAscDescByDate() {
        secondCertificate.setCreateDate(new Date());
        expectedList.add(secondCertificate);
        assertTrue(service.sortByAscDesc("date", "asc", expectedList).get(0).equals(certificate));
    }

    @Test
    public void sortByAscDescWithInvalidSortField() {
        expectedList.add(secondCertificate);
        expectedException.expect(IllegalArgumentException.class);
        service.sortByAscDesc("price", "asc", expectedList).get(0).equals(certificate);
    }

    @Test
    public void sortByAscDescWithInvalidSortOrderField() {
        expectedList.add(secondCertificate);
        expectedException.expect(IllegalArgumentException.class);
        service.sortByAscDesc("price", "asc", expectedList).get(0).equals(certificate);
    }

    @Test
    public void getAllCertificatesByTagName() {
        expectedList.add(secondCertificate);
        when(certificateDao.findAll()).thenReturn(expectedList);
        when(tagDao.findTagByName("tag")).thenReturn(tag);
        assertTrue(service.getAllCertificatesByTagName(tag.getName()).equals(expectedList));
    }

    @Test
    public void searchByPartOfName() {
        expectedList.add(secondCertificate);
        List<Certificate> actual = new ArrayList<>();
        actual.add(secondCertificate);
        when(certificateDao.findAll()).thenReturn(expectedList);
        assertTrue(service.searchByPartOfName("2").equals(actual));
    }

    @Test
    public void searchByPartOfDescription() {
        expectedList.add(secondCertificate);
        List<Certificate> actual = new ArrayList<>();
        when(certificateDao.findAll()).thenReturn(expectedList);
        assertTrue(service.searchByPartOfDescription("2").equals(actual));
    }
}