package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.model.GiftCertificate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class GiftCertificateDaoImplementationTest {
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
        GiftCertificate giftCertificateToAdd = new GiftCertificate();
        giftCertificateToAdd.setName("n4");
        giftCertificateToAdd.setDescription("s4");
        giftCertificateToAdd.setPrice(12);
        giftCertificateToAdd.setDuration(2);
        giftCertificateToAdd.setCreateDate(new Date());
        giftCertificateToAdd.setLastUpdateDate(new Date());
        dao.create(giftCertificateToAdd);

        List<GiftCertificate> defaultList = dao.read();
        assertTrue(defaultList.size() == 4);
    }

    @Test
    public void readAllTest() {
        List<GiftCertificate> defaultList = dao.read();
        assertTrue(defaultList.size() == 3);
    }

    @Test
    public void readByIdTest() {
        GiftCertificate actualGiftCertificate = dao.read(1l);
        assertTrue(actualGiftCertificate.getId() == 1l);
    }

    @Test
    public void deleteTest() {
        List<GiftCertificate> defaultList = dao.read();
        dao.delete(1);
        List<GiftCertificate> updatedList = dao.read();
        assertTrue(updatedList.size() == 2);
    }
}