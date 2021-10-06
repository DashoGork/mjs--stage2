package com.epam.esm.dao.tagGiftCertificate.impl;

import com.epam.esm.dao.tagGiftCertificate.TagCertificateDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class TagCertificateDaoImplementation implements TagCertificateDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String DELETE_TAG = "delete from mjs2.tag_gift_certificate where tag_id=?";
    private static final String SELECT_ALL_CERTIFICATES_BY_TAG = "select certificate_id from mjs2.tag_gift_certificate where tag_id=?";
    private static final String DELETE_CERTIFICATE = "delete from mjs2.tag_gift_certificate where certificate_id=?";
    private static final String SAVE_CERTIFICATE = "insert into mjs2.tag_gift_certificate (tag_id,certificate_id)  Values(?,?)";
    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "select tag_id from mjs2.tag_gift_certificate where certificate_id=?";


    @Autowired
    public TagCertificateDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void add(Tag tag, GiftCertificate certificate) {
        jdbcTemplate.update(SAVE_CERTIFICATE, tag.getId(), certificate.getId());
    }

    @Override
    public List<Long> readByTag(long id) {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES_BY_TAG, new RowMapper<Long>() {
            public Long mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getLong(1);
            }
        }, new Object[]{id});
    }

    @Override
    public List<Long> readByCertificate(long id) {
        return jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, new RowMapper<Long>() {
            public Long mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                return rs.getLong(1);
            }
        }, new Object[]{id});
    }

    @Override
    public void deleteTag(long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public void deleteCertificate(long id) {
        jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }
}
