package com.epam.esm.service.dto.certificate;

import com.epam.esm.mapper.certificate.CertificateDtoMapperImplementation;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.service.entity.certificate.CertificateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CertificateDtoServiceTest {
    @Mock
    private CertificateService service;
    private CertificateDtoMapperImplementation mapper;
    private CertificateDtoService certificateDtoService;

    private CertificateDto certificateDto;
    private Certificate certificate;

    @Before
    public void setUp() throws Exception {
        mapper = new CertificateDtoMapperImplementation();
        certificateDtoService = new CertificateDtoService(mapper, service);
        certificate = new Certificate();
        certificate.setCreateDate(new Date());
        certificate.setLastUpdateDate(new Date());
        certificate.setDuration(2);
        certificate.setPrice(12);
        certificate.setDescription("desc");
        certificate.setId(1l);
        certificate.setName("name");
        certificateDto = mapper.certificateToCertificateDto(certificate);
    }

    @Test
    public void create() {
        when(service.create(any())).thenReturn(certificate);
        assertTrue(certificateDtoService.create(certificateDto).equals(certificateDto));
        verify(service).create(any());

    }

    @Test
    public void delete() {
        doNothing().when(service).delete(any());
        certificateDtoService.delete(certificateDto);
        verify(service).delete(any());
    }

    @Test
    public void read() {
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        when(service.read()).thenReturn(certificateList);
        assertTrue(certificateDtoService.read().size() == 1);
        verify(service).read();
    }

    @Test
    public void testRead() {
        when(service.read(1l)).thenReturn(certificate);
        assertTrue(certificateDtoService.read(1l).getName().equals(certificate.getName()));
        verify(service).read(1l);
    }

    @Test
    public void patch() {
        doNothing().when(service).patch(1l, any());
        certificateDtoService.patch(1l, certificateDto);
        verify(service).patch(1l, any());
    }

    @Test
    public void getByTagOrQueryAndSort() {
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        when(service.getCertificatesByCriteria("", "", "", "", ""))
                .thenReturn(certificateList);
        assertTrue(certificateDtoService.getByTagOrQueryAndSort("", "", "", "", "").size() == 1);
    }

    @Test
    public void findPaginated() {
        List<Certificate> certificateList = new ArrayList<>();
        certificateList.add(certificate);
        when(service.findPaginated("", "", "", "", "", 1, 1))
                .thenReturn(certificateList);
        assertTrue(certificateDtoService.findPaginated("", "", "", "", "", 1, 1).size() == 1);
    }
}