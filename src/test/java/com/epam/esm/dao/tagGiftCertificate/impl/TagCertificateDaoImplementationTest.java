/*
package com.epam.esm.dao.tagGiftCertificate.impl;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

public class TagCertificateDaoImplementationTest {

    private TagCertificateDaoImplementation dao;

    @Before
    public void setUp() {
        DataSource embeddedDatabase = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:test-data.sql")
                .build();
        dao = new TagCertificateDaoImplementation(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    public void add() {
        Tag tag = new Tag();
        tag.setId(3);
        Certificate certificate = new Certificate();
        certificate.setId(3);
        dao.add(tag, certificate);
        List<Long> listOfCertificatesWithTag = dao.readByTag(3);
        assertTrue(listOfCertificatesWithTag.contains(3l));
    }

    @Test
    public void readByTag() {
        List<Long> listOfCertificatesWithTag = dao.readByTag(1);
        assertTrue(listOfCertificatesWithTag.contains(1l));
    }

    @Test
    public void readByCertificate() {
        List<Long> listOfTags = dao.readByCertificate(1);
        assertTrue(listOfTags.contains(1l));
    }

    @Test
    public void deleteTag() {
        dao.deleteTag(1l);
        List<Long> listOfCertificatesWithTag = dao.readByTag(1);
        assertTrue(listOfCertificatesWithTag.isEmpty());
    }

    @Test
    public void deleteCertificate() {
        dao.deleteCertificate(1l);
        List<Long> listOfTags = dao.readByCertificate(1);
        assertTrue(listOfTags.isEmpty());
    }
}*/
