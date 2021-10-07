package com.epam.esm.service.tag;

import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {
    @Mock
    private TagDaoImplementation dao;

    private TagService service;
    private Tag tag;

    @Before
    public void setUp() {
        service = new TagService(dao);
        tag = new Tag();
        tag.setName("name");
        tag.setId(1);
    }

    @Test
    public void create() {
        doNothing().when(dao).create(tag);
        service.create(tag);
    }

    @Test
    public void delete() {
        doNothing().when(dao).delete(tag.getId());
        service.delete(tag);
    }

    @Test
    public void read() {
        List<Tag> expectedList = new ArrayList<>();
        expectedList.add(tag);
        when(dao.read()).thenReturn(expectedList);
        List<Tag> actualList = service.read();
        assertTrue(actualList.equals(expectedList));
    }

    @Test
    public void testReadByName() {
        when(dao.read("name")).thenReturn(tag);
        Tag actualTag = service.read("name");
        assertTrue(actualTag.equals(tag));
    }

    @Test
    public void testReadByNameWithNullName() {
        Tag actualTag = service.read(null);
        assertTrue(actualTag.getId() == 0);
    }

    @Test
    public void testReadByNameWithNotFoundException() {
        when(dao.read("name")).thenThrow(new TagNotFoundException("message"));
        Tag actualTag = service.read("name");
        assertTrue(actualTag.getId() == 0);
    }

    @Test
    public void testReadById() {
        when(dao.read(1)).thenReturn(tag);
        Tag actualTag = service.read(1);
        assertTrue(actualTag.equals(tag));
    }

    @Test
    public void testReadByIdWithNotFoundException() {
        when(dao.read(22)).thenThrow(new TagNotFoundException("message"));
        Tag actualTag = service.read(22);
        assertTrue(actualTag.getId() == 0);
    }


}