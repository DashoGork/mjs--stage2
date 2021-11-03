/*
package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.model.entity.Certificate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class CertificateDaoImplementationTest {
    private GiftCertificateDaoImplementation dao;


    @Before
    public void setUp() {
        DataSource embeddedDatabase = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:test-data.sql")
                .build();
        dao = new GiftCertificateDaoImplementation(new JdbcTemplate(embeddedDatabase));
        dao.setRowMapper(new GiftCertificateRowMapper());
    }

    @Test
    public void createTest() {
        Certificate certificateToAdd = new Certificate();
        certificateToAdd.setName("n4");
        certificateToAdd.setDescription("s4");
        certificateToAdd.setPrice(12);
        certificateToAdd.setDuration(2);
        certificateToAdd.setCreateDate(new Date());
        certificateToAdd.setLastUpdateDate(new Date());
        dao.create(certificateToAdd);

        List<Certificate> defaultList = dao.read();
        assertTrue(defaultList.size() == 4);
    }

    @Test
    public void readAllTest() {
        List<Certificate> defaultList = dao.read();
        assertTrue(defaultList.size() == 3);
    }

    @Test
    public void readByIdTest() {
        Certificate actualCertificate = dao.read(1l);
        assertTrue(actualCertificate.getId() == 1l);
    }

    @Test
    public void deleteTest() {
        List<Certificate> defaultList = dao.read();
        dao.delete(1);
        List<Certificate> updatedList = dao.read();
        assertTrue(updatedList.size() == 2);
    }
}*/
