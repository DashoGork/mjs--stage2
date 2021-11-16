package com.epam.esm.service.dto.tag;

import com.epam.esm.mapper.tag.TagDtoMapperImplementation;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.tag.TagService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagDtoServiceTest {


    @Mock
    private TagService service;

    private TagDtoMapperImplementation mapper;
    private TagDtoService tagDtoService;
    private Tag tag;
    private TagDto tagDto;

    @Before
    public void setUp() throws Exception {
        mapper = new TagDtoMapperImplementation();
        tagDtoService = new TagDtoService(mapper, service);
        tag = new Tag();
        tag.setName("tag");
        tag.setId(1l);
        tagDto = mapper.tagToTagDto(tag);
    }

    @Test
    public void create() {
        when(service.create(any())).thenReturn(tag);
        assertTrue(tagDtoService.create(tagDto).equals(tagDto));
        verify(service).create(any());
    }

    @Test
    public void delete() {
        doNothing().when(service).delete(any());
        tagDtoService.delete(tagDto);
        verify(service).delete(any());
    }

    @Test
    public void read() {
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        when(service.read()).thenReturn(list);
        assertTrue(tagDtoService.read().size() == 1);
        verify(service).read();
    }

    @Test
    public void readById() {
        when(service.read(1l)).thenReturn(tag);
        assertTrue(tagDtoService.read(1l).getName().equals(tag.getName()));
        verify(service).read(1l);
    }

    @Test
    public void readByName() {
        when(service.read("tag")).thenReturn(tag);
        assertTrue(tagDtoService.read("tag").getName().equals(tag.getName()));
        verify(service).read("tag");
    }

    @Test
    public void findPaginated() {
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        when(service.findPaginated(1, 1))
                .thenReturn(list);
        assertTrue(tagDtoService.findPaginated(1, 1).size() == 1);
    }

    @Test
    public void getTagsOfUserWithHighestPriceOfOrders() {
        when(service.getTagsOfUserWithHighestPriceOfOrders()).thenReturn(tag);
        assertTrue(tagDtoService.getTagsOfUserWithHighestPriceOfOrders().getName().equals(tag.getName()));
    }
}