package com.epam.esm.service.entity.certificate;

import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
import com.epam.esm.mapper.certificate.CertificateDtoMapperImplementation;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CertificateServiceTest {
    @Mock
    private GiftCertificateDaoImplementation certificateDao;
    @Mock
    private TagDaoImplementation tagDao;
    @Mock
    private TagCertificateDaoImplementation tagCertificateDao;


    private CertificateService service;
    private Tag tag;
    private Certificate certificate;
    private List<Certificate> expectedList;
    private CertificateDtoMapperImplementation mapper;

    @Before
    public void setUp() throws Exception {
        service = new CertificateService(certificateDao, tagDao,
                tagCertificateDao);
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

    }

    @Test
    public void create() {
        doNothing().when(tagDao).create(tag);
        doNothing().when(certificateDao).create(certificate);
        when(certificateDao.read()).thenReturn(expectedList);
        certificate.setTags(new ArrayList<>());
        doNothing().when(tagCertificateDao).add(tag, certificate);
        service.create((certificate));
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
    public void testRead() {
        when(certificateDao.read(0)).thenReturn(certificate);
        Certificate actualCertificate = service.read(0);
        assertTrue(actualCertificate.getDescription().equals(certificate.getDescription()));
        assertTrue(actualCertificate.getName().equals(certificate.getName()));
    }

    @Test
    public void delete() {
        doNothing().when(certificateDao).delete(certificate.getId());
        doNothing().when(tagCertificateDao).deleteCertificate(certificate.getId());
        service.delete((certificate));
    }

    @Test
    public void update() {
    }

    @Test
    public void getAllCertificatesByTag() {
        List<Long> expectedListOfIds = new ArrayList<>();
        expectedListOfIds.add(1l);
        when(tagDao.read(tag.getName())).thenReturn(tag);
        when(tagCertificateDao.readByTag(tag.getId())).thenReturn(expectedListOfIds);
        when(certificateDao.read(1l)).thenReturn(certificate);
        List<Certificate> actualList =
                service.getAllCertificatesByTagName(tag.getName());
        assertTrue(actualList.equals(expectedList));
    }

    @Test
    public void getByPartOfNameOrDescription() {
        Certificate certificateToRange = new Certificate();
        certificateToRange.setDescription("ddd");
        certificateToRange.setName("nnnn");
        certificateToRange.setId(11);
        expectedList.add(certificateToRange);
        String query = "ddd nnnn";
        when(certificateDao.read()).thenReturn(expectedList);
        when(certificateDao.read(0)).thenReturn(certificate);
        when(certificateDao.read(11)).thenReturn(certificateToRange);
        List<Certificate> actualList =
                service.getByPartOfNameOrDescription(query);
        assertTrue(actualList.get(0).getDescription().equals(certificateToRange.getDescription()));
    }

    @Test
    public void sortByAscDesc() {
        Certificate certificateToRange = new Certificate();
        certificateToRange.setDescription("ddd");
        certificateToRange.setName("nnnn");
        certificateToRange.setId(11);
        expectedList.add(certificateToRange);
        when(certificateDao.read()).thenReturn(expectedList);
        when(certificateDao.read(0)).thenReturn(certificate);
        when(certificateDao.read(11)).thenReturn(certificateToRange);
        List<Certificate> actualListAsc = service.sortByAscDesc("", "name",
                "asc");
        List<Certificate> actualListDesc = service.sortByAscDesc("",
                "name", "desc");
        assertTrue(actualListAsc.get(0) != actualListDesc.get(0));
    }


}