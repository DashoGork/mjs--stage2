package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.model.entity.Certificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GiftCertificateRowMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate = new Certificate();
        certificate.setId(resultSet.getLong("id"));
        certificate.setName(resultSet.getString("name"));
        certificate.setDescription(resultSet.getString("description"));
        certificate.setPrice(resultSet.getInt("price"));
        certificate.setDuration(resultSet.getInt("duration"));
        certificate.setCreateDate(resultSet.getTime("create_date"));
        certificate.setLastUpdateDate(resultSet.getTime("last_update_date"));
        return certificate;
    }
}