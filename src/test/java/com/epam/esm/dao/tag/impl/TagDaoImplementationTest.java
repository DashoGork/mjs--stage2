package com.epam.esm.dao.tag.impl;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.dao.tag.impl.config.SpringMvcDispatcherServletInitializer;
import com.epam.esm.model.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-test.xml", classes = {SpringConfig.class, SpringMvcDispatcherServletInitializer.class})
class TagDaoImplementationTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private TagDaoImplementation tagDao;

    @org.junit.jupiter.api.Test
    void create() {
        Tag tag = new Tag();
        tag.setName("name");
        tagDao.create(tag);
        Tag tag12 = tagDao.read().stream().filter((tag1 -> tag1.getName().equals("name"))).findAny().orElse(null);
    }

    @org.junit.jupiter.api.Test
    void delete() {
    }

    @org.junit.jupiter.api.Test
    void read() {
    }

    @org.junit.jupiter.api.Test
    void testRead() {
    }

    @org.junit.jupiter.api.Test
    void testRead1() {
    }
}