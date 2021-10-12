package com.epam.esm.dao.tag.impl;

import com.epam.esm.dao.giftCertificate.impl.GiftCertificateDaoImplementation;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TagDaoImplementation implements TagDao {
    private static final String DELETE_TAG = "delete from mjs2.tag where id=?";
    private static final String SAVE_TAG = "insert into mjs2.tag (name) values (?)";
    private static final String SELECT_TAG_BY_ID = "select * from mjs2.tag where id=?";
    private static final String SELECT_TAG_BY_NAME = "select * from mjs2.tag where name=?";
    private static final String SELECT_ALL_TAGS = "select * from mjs2.tag";
    private final JdbcTemplate jdbcTemplate;
    private TagRowMapper rowMapper;

    @Autowired
    public TagDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setRowMapper(TagRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public void create(Tag tag) {
        log.info("Create tag with name = " + tag.getName());
        jdbcTemplate.update(SAVE_TAG, tag.getName());
    }

    @Override
    public void delete(long id) {
        log.info("Delete tag with id = " + id);
        jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public Tag read(long id) throws TagNotFoundException {
        log.info("Read tag with id = " + id);
        return jdbcTemplate.query(SELECT_TAG_BY_ID, rowMapper, new Object[]{id})
                .stream().findAny()
                .orElseThrow(() -> new TagNotFoundException("There isn't tag with such id" + id));
    }

    @Override
    public Tag read(String name) throws TagNotFoundException {
        log.info("Read tag with name = " + name);
        return jdbcTemplate.query(SELECT_TAG_BY_NAME, rowMapper, new Object[]{name})
                .stream().findAny().orElseThrow(() -> new TagNotFoundException("There isn't tag with such name" + name));
    }

    @Override
    public List<Tag> read() {
        log.info("Read all tags");
        return jdbcTemplate.query(SELECT_ALL_TAGS, rowMapper);
    }

    @Override
    public void update(String name) {
        log.info("Update tag with name = " + name);
        try {
            read(name);
        } catch (TagNotFoundException e) {
            Tag tag = new Tag();
            tag.setName(name);
            create(tag);
        }
    }
}