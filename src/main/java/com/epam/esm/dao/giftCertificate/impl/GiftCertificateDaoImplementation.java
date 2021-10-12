package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class GiftCertificateDaoImplementation implements GiftCertificateDao {

    private static final String DELETE_GIFT_CERTIFICATE = "delete from mjs2.gift_certificate where id=?";
    private static final String CREATE_GIFT_CERTIFICATE = "insert into " +
            "mjs2.gift_certificate (name, price,description,duration,create_date," +
            "last_update_date) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SELECT_GIFT_CERTIFICATE_BY_ID = "select * from mjs2.gift_certificate where id=?";
    private static final String SELECT_ALL_GIFT_CERTIFICATE = "select * from mjs2.gift_certificate";
    private static final String UPDATE_GIFT_CERTIFICATE = "update mjs2" +
            ".gift_certificate set name=?, description=?, " +
            "price=?, " +
            "duration=?, last_update_date=NOW()\n" +
            "WHERE id=?";
    private static final String PATCH_GIFT_CERTIFICATE = "update mjs2" +
            ".gift_certificate set name=COALESCE(?,?), description=COALESCE(?,?), " +
            "price=COALESCE(?,?), " +
            "duration=COALESCE(?,?), last_update_date=NOW()\n" +
            "WHERE id=?";
    private static final String SELECT_GIFT_CERTIFICATE_BY_CREATE_DATE =
            "select * from mjs2.gift_certificate where create_date=?";
    private final JdbcTemplate jdbcTemplate;
    private GiftCertificateRowMapper rowMapper;

    @Autowired
    public GiftCertificateDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setRowMapper(GiftCertificateRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        log.info("GiftCertificate created" + giftCertificate.toString());
        jdbcTemplate.update(CREATE_GIFT_CERTIFICATE,
                giftCertificate.getName(),
                giftCertificate.getPrice(), giftCertificate.getDescription(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate());
    }

    @Override
    public List<GiftCertificate> read() {
        log.info("Read all giftCertificates");
        return jdbcTemplate.query(SELECT_ALL_GIFT_CERTIFICATE, rowMapper);
    }


    @Override
    public GiftCertificate read(long id) throws GiftCertificateNotFoundException {
        log.info("Read giftCertificate with id =" + id);
        return jdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_ID, rowMapper,
                        new Object[]{id})
                .stream().findAny().orElseThrow(() -> new GiftCertificateNotFoundException("Certificate wasn't found. id = " + id));
    }

    @Override
    public GiftCertificate read(Date date) throws GiftCertificateNotFoundException {
        log.info("Read giftCertificate with date =" + date);
        return jdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_CREATE_DATE,
                        rowMapper, new Object[]{date})
                .stream().findAny().orElseThrow(() -> new GiftCertificateNotFoundException("Certificate wasn't found. date = " + date));
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        log.info("Update giftCertificate with id = " + giftCertificate.getId());
        jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getId());
    }

    @Override
    public void patch(GiftCertificate patchedCertificate, GiftCertificate oldCertificate) {
        jdbcTemplate.update(PATCH_GIFT_CERTIFICATE,
                patchedCertificate.getName(), oldCertificate.getName(),
                patchedCertificate.getDescription(), oldCertificate.getDescription(),
                patchedCertificate.getPrice(), oldCertificate.getPrice(),
                patchedCertificate.getDuration(), oldCertificate.getDuration(),
                oldCertificate.getId());
    }

    @Override
    public void delete(long id) {
        log.info("Delete giftCertificate with id = " + id);
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id);
    }
}