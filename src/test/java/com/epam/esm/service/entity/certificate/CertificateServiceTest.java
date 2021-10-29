//package com.epam.esm.service.entity.certificate;
//
//import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
//import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
//import com.epam.esm.exceptions.GiftCertificateNotFoundException;
//import com.epam.esm.mapper.certificate.CertificateDtoMapperImplementation;
//import com.epam.esm.model.entity.Certificate;
//import com.epam.esm.model.entity.Tag;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CertificateServiceTest {
//    @Mock
//    private GiftCertificateDaoImplementation certificateDao;
//    @Mock
//    private TagDaoImplementation tagDao;
//    @Mock
//    private TagCertificateDaoImplementation tagCertificateDao;
//
//
//    private CertificateService service;
//    private Tag tag;
//    private Certificate certificate;
//    private List<Certificate> expectedList;
//    private CertificateDtoMapperImplementation mapper;
//
//    @Before
//    public void setUp() throws Exception {
//        service = new CertificateService(certificateDao, tagDao,
//                tagCertificateDao);
//        tag = new Tag();
//        tag.setName("name");
//        tag.setId(1);
//        certificate = new Certificate();
//        certificate.setName("name");
//        certificate.setDescription("desc");
//        certificate.setDuration(2);
//        certificate.setPrice(12);
//        certificate.setCreateDate(new Date());
//        certificate.setLastUpdateDate(new Date());
//        expectedList = new ArrayList<>();
//        expectedList.add(certificate);
//    }
//
//    @Test
//    public void createAlreadyExisting() {
//        doNothing().when(tagDao).create(tag);
//        doNothing().when(certificateDao).create(certificate);
//        when(certificateDao.read(anyString())).thenReturn(certificate);
//        certificate.setTags(new ArrayList<>());
//        doNothing().when(tagCertificateDao).add(tag, certificate);
//        Certificate actualCertificate = service.create((certificate));
//        assertTrue(actualCertificate.equals(certificate));
//    }
//
//    @Test
//    public void create() {
//        doNothing().when(tagDao).create(tag);
//        doNothing().when(certificateDao).create(certificate);
//        when(certificateDao.read("name")).thenThrow(new GiftCertificateNotFoundException("not found"));
//        certificate.setTags(new ArrayList<>());
//        doNothing().when(tagCertificateDao).add(tag, certificate);
//        when(certificateDao.read((Date) any())).thenReturn(certificate);
//        service.create((certificate));
//        verify(certificateDao).create(certificate);
//    }
//
//    @Test
//    public void read() {
//        List<Certificate> expected = new ArrayList<>();
//        expected.add(certificate);
//        when(certificateDao.read()).thenReturn(expected);
//        List<Certificate> actualList = service.read();
//        assertTrue(actualList.equals(expected));
//    }
//
//    @Test
//    public void testRead() {
//        when(certificateDao.read(0)).thenReturn(certificate);
//        Certificate actualCertificate = service.read(0);
//        assertTrue(actualCertificate.getDescription().equals(certificate.getDescription()));
//        assertTrue(actualCertificate.getName().equals(certificate.getName()));
//    }
//
//    @Test
//    public void delete() {
//        doNothing().when(certificateDao).delete(certificate.getId());
//        doNothing().when(tagCertificateDao).deleteCertificate(certificate.getId());
//        service.delete((certificate));
//        verify(certificateDao).delete(certificate.getId());
//    }
//
//    @Test
//    public void getAllCertificatesByTag() {
//        List<Long> expectedListOfIds = new ArrayList<>();
//        expectedListOfIds.add(1l);
//        when(tagDao.read(tag.getName())).thenReturn(tag);
//        when(tagCertificateDao.readByTag(tag.getId())).thenReturn(expectedListOfIds);
//        when(certificateDao.read(1l)).thenReturn(certificate);
//        List<Certificate> actualList =
//                service.getAllCertificatesByTagName(tag.getName());
//        assertTrue(actualList.equals(expectedList));
//    }
//
//    @Test
//    public void patch() {
//        Certificate patchedCertificate = new Certificate();
//        patchedCertificate.setPrice(300);
//        when(certificateDao.read(0)).thenReturn(certificate);
//        doNothing().when(certificateDao).patch(patchedCertificate,
//                certificate);
//        when(tagCertificateDao.readByCertificate(0)).thenReturn(new ArrayList<>());
//        service.patch(0, patchedCertificate);
//        verify(certificateDao).patch(patchedCertificate, certificate);
//    }
//
//    @Test
//    public void getCertificatesByCriteriaWithoutSortOptionAndQuery() {
//        when(certificateDao.read()).thenReturn(expectedList);
//        assertTrue(service.getCertificatesByCriteria("", "", "", "", "").equals(expectedList));
//    }
//
//    @Test
//    public void getCertificatesByCriteria() {
//
//    }
//
//    @Test
//    public void searchByPartOfName() {
//        Certificate certificate = new Certificate();
//        certificate.setName("11111");
//        expectedList.add(certificate);
//        when(certificateDao.read()).thenReturn(expectedList);
//        assertTrue(service.searchByPartOfName("1").size() == 1);
//        assertTrue(service.searchByPartOfName("1").get(0).equals(certificate));
//    }
//
//    @Test
//    public void searchByPartOfDescription() {
//        Certificate additionalCertificate = new Certificate();
//        additionalCertificate.setDescription("11111");
//        expectedList.add(additionalCertificate);
//        when(certificateDao.read()).thenReturn(expectedList);
//        assertTrue(service.searchByPartOfDescription("desc").size() == 1);
//        assertTrue(service.searchByPartOfDescription("desc").get(0).equals(certificate));
//        assertTrue(service.searchByPartOfDescription("1").size() == 1);
//        assertTrue(service.searchByPartOfDescription("1").get(0).equals(additionalCertificate));
//    }
//}
