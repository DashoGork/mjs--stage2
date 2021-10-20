package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.dao.AbstractDao;
import com.epam.esm.dao.giftCertificate.GiftCertificateDao;
import com.epam.esm.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.model.Certificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class GiftCertificateDaoImplementation extends AbstractDao<Certificate> implements GiftCertificateDao {

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
    private static final String SELECT_GIFT_CERTIFICATE_BY_NAME =
            "select * from mjs2.gift_certificate where name=?";
    private GiftCertificateRowMapper rowMapper;

    @Autowired
    public GiftCertificateDaoImplementation(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Autowired
    public void setRowMapper(GiftCertificateRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    @Transactional
    public void create(Certificate certificate) {
        log.info("GiftCertificate created" + certificate.toString());
        super.getJdbcTemplate().update(CREATE_GIFT_CERTIFICATE,
                certificate.getName(),
                certificate.getPrice(), certificate.getDescription(),
                certificate.getDuration(),
                certificate.getCreateDate(),
                certificate.getLastUpdateDate());
    }

    @Override
    public List<Certificate> read() {
        log.info("Read all giftCertificates");
        return super.read(SELECT_ALL_GIFT_CERTIFICATE, rowMapper);
    }

    public Certificate read(String name) {
        log.info("Read giftCertificate by name");
        return super.getJdbcTemplate().query(SELECT_GIFT_CERTIFICATE_BY_NAME,
                        rowMapper, new Object[]{name})
                .stream().findAny().orElseThrow(() -> new GiftCertificateNotFoundException("Certificate wasn't found. name = " + name));
    }

    @Override
    public Certificate read(long id) throws GiftCertificateNotFoundException {
        log.info("Read giftCertificate with id =" + id);
        try {
            return super.read(SELECT_GIFT_CERTIFICATE_BY_ID, id, rowMapper);
        } catch (Throwable e) {
            throw new GiftCertificateNotFoundException("Certificate wasn't " +
                    "found. id = " + id);
        }
    }

    @Override
    public Certificate read(Date date) throws GiftCertificateNotFoundException {
        log.info("Read giftCertificate with date =" + date);
        return super.getJdbcTemplate().query(SELECT_GIFT_CERTIFICATE_BY_CREATE_DATE,
                        rowMapper, new Object[]{date})
                .stream().findAny().orElseThrow(() -> new GiftCertificateNotFoundException("Certificate wasn't found. date = " + date));
    }

    @Override
    public void update(Certificate certificate) {
        log.info("Update giftCertificate with id = " + certificate.getId());
        super.getJdbcTemplate().update(UPDATE_GIFT_CERTIFICATE, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), certificate.getId());
    }

    @Override
    @Transactional
    public void patch(Certificate patchedCertificate, Certificate oldCertificate) {
        super.getJdbcTemplate().update(PATCH_GIFT_CERTIFICATE,
                patchedCertificate.getName(), oldCertificate.getName(),
                patchedCertificate.getDescription(), oldCertificate.getDescription(),
                patchedCertificate.getPrice(), oldCertificate.getPrice(),
                patchedCertificate.getDuration(), oldCertificate.getDuration(),
                oldCertificate.getId());
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Delete giftCertificate with id = " + id);
        super.delete(DELETE_GIFT_CERTIFICATE, id);
    }
}