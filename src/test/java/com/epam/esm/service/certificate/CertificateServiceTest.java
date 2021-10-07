package com.epam.esm.service.certificate;

import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.dao.tagGiftCertificate.impl.TagCertificateDaoImplementation;
import com.epam.esm.enums.SortOptions;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.tag.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
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
    private GiftCertificate giftCertificate;
    private List<GiftCertificate> expectedList;

    @Before
    public void setUp() throws Exception {
        service = new CertificateService(certificateDao,tagDao,tagCertificateDao);
        tag = new Tag();
        tag.setName("name");
        tag.setId(1);
        giftCertificate=new GiftCertificate();
        giftCertificate.setName("name");
        giftCertificate.setDescription("desc");
        giftCertificate.setDuration(2);
        giftCertificate.setPrice(12);
        giftCertificate.setCreateDate(new Date());
        giftCertificate.setLastUpdateDate(new Date());
        expectedList = new ArrayList<>();
        expectedList.add(giftCertificate);

    }

    @Test
    public void create() {
        doNothing().when(tagDao).create(tag);
        doNothing().when(certificateDao).create(giftCertificate);
        doNothing().when(tagCertificateDao).add( tag,giftCertificate);
        service.create(giftCertificate,tag);
    }

    @Test
    public void read() {
        List<GiftCertificate> expected = new ArrayList<>();
        expected.add(giftCertificate);
        when(certificateDao.read()).thenReturn(expected);
        List<GiftCertificate> actualList = service.read();
        assertTrue(actualList.equals(expected));
    }

    @Test
    public void testRead() {
        when(certificateDao.read(0)).thenReturn(giftCertificate);
        GiftCertificate actualCertificate = service.read(0);
        assertTrue(actualCertificate.getDescription().equals(giftCertificate.getDescription()));
        assertTrue(actualCertificate.getName().equals(giftCertificate.getName()));
    }

    @Test
    public void delete() {
        doNothing().when(certificateDao).delete(giftCertificate.getId());
        doNothing().when(tagCertificateDao).deleteCertificate(giftCertificate.getId());
        service.delete(giftCertificate);
    }

    @Test
    public void update() {
    }

    @Test
    public void getAllCertificatesByTag() {
        List<Long> expectedListOfIds =new ArrayList<>();
        expectedListOfIds.add(1l);
        when(tagDao.read(tag.getName())).thenReturn(tag);
        when(tagCertificateDao.readByTag(tag.getId())).thenReturn(expectedListOfIds);
        when(certificateDao.read(1l)).thenReturn(giftCertificate);
        List<GiftCertificate> actualList = service.getAllCertificatesByTag(tag);
        assertTrue(actualList.equals(expectedList));
    }

    @Test
    public void getByPartOfNameOrDescription() {
        GiftCertificate giftCertificateToRange = new GiftCertificate();
        giftCertificateToRange.setDescription("ddd");
        giftCertificateToRange.setName("nnnn");
        giftCertificateToRange.setId(11);
        expectedList.add(giftCertificateToRange);
        String query = "ddd nnnn";
        when(certificateDao.searchByPartOfDescription(query)).thenReturn(expectedList);
        when(certificateDao.searchByPartOfName(query)).thenReturn(expectedList);
        List<GiftCertificate> actualList = service.getByPartOfNameOrDescription(query);
        assertTrue(actualList.get(0).getDescription().equals(giftCertificateToRange.getDescription()));
    }

    @Test
    public void sortByAscDesc() {
        GiftCertificate giftCertificateToRange = new GiftCertificate();
        giftCertificateToRange.setDescription("ddd");
        giftCertificateToRange.setName("nnnn");
        giftCertificateToRange.setId(11);
        expectedList.add(giftCertificateToRange);
        String query = "ddd nnnn";
        when(certificateDao.searchByPartOfDescription(query)).thenReturn(expectedList);
        when(certificateDao.searchByPartOfName(query)).thenReturn(expectedList);
        List<GiftCertificate> actualListAsc = service.SortByAscDesc(query, "name", "asc");
        List<GiftCertificate> actualListDesc = service.SortByAscDesc(query, "name", "desc");
        assertTrue(actualListAsc.get(0)!=actualListDesc.get(0));
    }
}