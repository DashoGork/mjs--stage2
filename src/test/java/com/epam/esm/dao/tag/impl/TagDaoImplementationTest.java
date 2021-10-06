package com.epam.esm.dao.tag.impl;

import com.epam.esm.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.Assert.*;

public class TagDaoImplementationTest {

    private TagDaoImplementation dao;

    @Before
    public void setUp() throws Exception {
        DataSource embeddedDatabase = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:schema.sql")
                .addScript("classpath:test-data.sql")
                .build();
        dao = new TagDaoImplementation(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    public void create() {
        Tag tag = new Tag();
        tag.setName("name4");
        List<Tag> defaultList = dao.read();
        dao.create(tag);
        List<Tag> updatedList = dao.read();
        assertTrue(updatedList.size() == 4);
    }

    @Test
    public void delete() {
        List<Tag> defaultList = dao.read();
        dao.delete(1);
        List<Tag> updatedList = dao.read();
        assertTrue(defaultList.size() - updatedList.size() == 1);

    }

    @Test
    public void read() {
        List<Tag> defaultList = dao.read();
        assertTrue(defaultList.size() == 3);
    }

    @Test
    public void testRead() {
        String name = "name1";
        Tag tag = dao.read(name);
        assertTrue(tag.getName().equals(name));
    }

    @Test
    public void testRead1() {
        Tag tag = dao.read(1);
        assertTrue(tag.getId() == 1l);
    }

    @Test
    public void update() {
        String existingName = "name1";
        List<Tag> defaultList = dao.read();
        dao.update(existingName);
        List<Tag> updatedList = dao.read();
        assertTrue(defaultList.size() == updatedList.size());
        String newName = "name5";
        dao.update(newName);
        List<Tag> reallyUpdatedList = dao.read();
        assertTrue(reallyUpdatedList.size() - defaultList.size() == 1);
    }
}