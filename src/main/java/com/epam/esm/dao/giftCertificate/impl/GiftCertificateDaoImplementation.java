package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDaoImplementation implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;
    private static Logger log = Logger.getLogger(GiftCertificateDaoImplementation.class.getName());

    private static final String DELETE_GIFT_CERTIFICATE = "delete from mjs2.gift_certificate where id=?";
    private static final String SAVE_GIFT_CERTIFICATE = "insert into mjs2.gift_certificate (name, price,description,duration,create_date=now(),last_update_date) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SELECT_GIFT_CERTIFICATE_BY_ID = "select * from mjs2.gift_certificate where id=?";
    private static final String SELECT_ALL_GIFT_CERTIFICATE = "select * from mjs2.gift_certificate";
    private static final String UPDATE_GIFT_CERTIFICATE = "update mjs2.gift_certificate set name=?, description=?, price=?, duration=?, last_update_date=now()\n" +
            "WHERE id=?";

    @Autowired
    public GiftCertificateDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        log.info("GiftCertificate created" + giftCertificate.toString());
        jdbcTemplate.update(SAVE_GIFT_CERTIFICATE,
                new Object[]{giftCertificate.getName(), giftCertificate.getPrice(), giftCertificate.getDescription(), giftCertificate.getDuration(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate()});
    }

    @Override
    public List<GiftCertificate> read() {
        log.info("Read all giftCertificates");
        return jdbcTemplate.query(SELECT_ALL_GIFT_CERTIFICATE, new GiftCertificateRowMapper());
    }

    @Override
    public GiftCertificate read(long id) throws GiftCertificateNotFoundException {
        log.info("Read giftCertificate with id =" + id);
        return jdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_ID, new GiftCertificateRowMapper(), new Object[]{id})
                .stream().findAny().orElseThrow(() -> new GiftCertificateNotFoundException("Certificate wasn't found. id = " + id));
    }

    @Override
    public List<GiftCertificate> searchByPartOfDescription(String query) {
        log.info("Search giftCertificates by part of description with query = " + query);
        List<GiftCertificate> listOfAll = read();
        return listOfAll.stream()
                .filter((giftCertificate ->
                        giftCertificate.getDescription().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificate> searchByPartOfName(String query) {
        log.info("Search giftCertificates by part of name with query = " + query);
        List<GiftCertificate> listOfAll = read();
        return listOfAll.stream()
                .filter((giftCertificate ->
                        giftCertificate.getName().toLowerCase().contains(query.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        log.info("Update giftCertificate with id = " + giftCertificate.getId());
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getId());
    }

    @Override
    public void delete(long id) {
        log.info("Delete giftCertificate with id = " + id);
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id);
    }
}
