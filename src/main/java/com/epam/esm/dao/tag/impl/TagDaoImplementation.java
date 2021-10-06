package com.epam.esm.dao.tag.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class TagDaoImplementation implements TagDao {
    private final JdbcTemplate jdbcTemplate;

    private static final String DELETE_TAG = "delete from mjs2.tag where id=?";
    private static final String SAVE_TAG = "insert into mjs2.tag (name) values (?)";
    private static final String SELECT_TAG_BY_ID = "select * from mjs2.tag where id=?";
    private static final String SELECT_TAG_BY_NAME = "select * from mjs2.tag where name=?";
    private static final String SELECT_ALL_TAGS = "select * from mjs2.tag";

    @Autowired
    public TagDaoImplementation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Tag tag) {
        jdbcTemplate.update(SAVE_TAG, tag.getName());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_TAG, id);
    }

    @Override
    public Tag read(long id) throws TagNotFoundException {
        return jdbcTemplate.query(SELECT_TAG_BY_ID, new TagRowMapper(), new Object[]{id})
                .stream().findAny().orElseThrow(() -> new TagNotFoundException("There isn't tag with such id" + id));
    }

    @Override
    public Tag read(String name) throws TagNotFoundException {
        return jdbcTemplate.query(SELECT_TAG_BY_NAME, new TagRowMapper(), new Object[]{name})
                .stream().findAny().orElseThrow(() -> new TagNotFoundException("There isn't tag with such name" + name));
    }

    @Override
    public List<Tag> read() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, new TagRowMapper());
    }

    @Override
    public void update(String name) {
        try {
            read(name);
        } catch (TagNotFoundException e) {
            Tag tag = new Tag();
            tag.setName(name);
            create(tag);
        }
    }
}
