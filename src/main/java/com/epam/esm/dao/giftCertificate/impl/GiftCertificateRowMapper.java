package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int i) throws SQLException {
        GiftCertificate giftCertificate=new GiftCertificate();
        giftCertificate.setId(resultSet.getLong("id"));
        giftCertificate.setName(resultSet.getString("name"));
        giftCertificate.setDescription(resultSet.getString("description"));
        giftCertificate.setPrice(resultSet.getInt("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setCreateDate(resultSet.getTime("create_date"));
        giftCertificate.setLastUpdateDate(resultSet.getTime("last_update_date"));
        return giftCertificate;
    }
}