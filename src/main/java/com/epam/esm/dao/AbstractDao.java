package com.epam.esm.dao;

import com.epam.esm.exceptions.BaseNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Setter
@Getter
public abstract class AbstractDao<BaseEntity> {
    private final JdbcTemplate jdbcTemplate;

    public AbstractDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected List<BaseEntity> read(String sql, RowMapper rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    protected BaseEntity read(String sql, long id, RowMapper<BaseEntity> rowMapper) throws Throwable {
        return jdbcTemplate.query(sql, rowMapper,
                        new Object[]{id}).stream()
                .findAny().orElseThrow(() -> new BaseNotFoundException("BaseEntity wasn't " +
                        "found. id = " + id));
    }

    protected void delete(String sql, long id) {
        jdbcTemplate.update(sql, id);
    }
}