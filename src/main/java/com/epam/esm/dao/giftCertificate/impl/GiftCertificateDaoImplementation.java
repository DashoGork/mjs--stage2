package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GiftCertificateDaoImplementation implements GiftCertificateDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String DELETE_GIFT_CERTIFICATE = "delete * from gift_certificate where id=?";
    private static final String SAVE_GIFT_CERTIFICATE = "insert into gift_certificate VALUES(?, ?, ?, ?, ?, ?)";
    private static final String SELECT_GIFT_CERTIFICATE_BY_ID = "select * from gift_certificate where id=?";
    private static final String SELECT_ALL_GIFT_CERTIFICATE = "select * from gift_certificate";
    private static final String UPDATE_GIFT_CERTIFICATE = "update * from gift_certificate";

    @Autowired
    public GiftCertificateDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SAVE_GIFT_CERTIFICATE, giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate());
    }

    @Override
    public List<GiftCertificate> read() {
        return jdbcTemplate.query(SELECT_ALL_GIFT_CERTIFICATE,new GiftCertificateRowMapper());
    }

    @Override
    public GiftCertificate read(long id) throws GiftCertificateNotFoundException {
        return jdbcTemplate.query(SELECT_GIFT_CERTIFICATE_BY_ID ,new GiftCertificateRowMapper(), new Object[]{id})
                .stream().findAny().orElseThrow(()->new GiftCertificateNotFoundException("Certificate wasn't found. id = "+id));
    }


    //
    @Override
    public void update(GiftCertificate giftCertificate) {

    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_GIFT_CERTIFICATE, id);
    }
}
