package com.epam.esm.dao.tag.impl;

import com.epam.esm.dao.AbstractDao;
import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class TagDaoImplementation extends AbstractDao<Tag> implements TagDao {
    private static final String DELETE_TAG = "delete from mjs2.tag where id=?";
    private static final String SAVE_TAG = "insert into mjs2.tag (name) values (?)";
    private static final String SELECT_TAG_BY_ID = "select * from mjs2.tag where id=?";
    private static final String SELECT_TAG_BY_NAME = "select * from mjs2.tag where name=?";
    private static final String SELECT_ALL_TAGS = "select * from mjs2.tag";
    private TagRowMapper rowMapper;

    @Autowired
    public TagDaoImplementation(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Autowired
    public void setRowMapper(TagRowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    @Transactional
    public void create(Tag tag) {
        log.info("Create tag with name = " + tag.getName());
        super.getJdbcTemplate().update(SAVE_TAG, tag.getName());
    }

    @Override
    @Transactional
    public void delete(long id) {
        log.info("Delete tag with id = " + id);
        super.delete(DELETE_TAG, id);
    }

    @Override
    public Tag read(long id) throws TagNotFoundException {
        log.info("Read tag with id = " + id);
        try {
            return super.read(SELECT_TAG_BY_ID, id, rowMapper);
        } catch (Throwable e) {
            throw new TagNotFoundException("Tag wasn't" +
                    " found. id =" + id);
        }
    }

    @Override
    public Tag read(String name) throws TagNotFoundException {
        log.info("Read tag with name = " + name);
        return super.getJdbcTemplate().query(SELECT_TAG_BY_NAME, rowMapper, new Object[]{name})
                .stream().findAny().orElseThrow(() -> new TagNotFoundException("There isn't tag with such name" + name));
    }

    @Override
    public List<Tag> read() {
        log.info("Read all tags");
        return super.read(SELECT_ALL_TAGS, rowMapper);
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