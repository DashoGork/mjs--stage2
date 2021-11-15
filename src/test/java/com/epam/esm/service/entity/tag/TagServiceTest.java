package com.epam.esm.service.entity.tag;

import com.epam.esm.dao.order.impl.OrderDao;
import com.epam.esm.dao.tag.impl.TagDao;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.entity.Tag;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private TagDao tagDao;
    @Mock
    private TagDao tagDaoI;
    @Mock
    private OrderDao orderDao;

    private TagService service;
    private Tag tag;
    private Tag secondTag;
    private List<Tag> listOfAll;

    @Before
    public void setUp() throws Exception {
        service = new TagService(tagDaoI);
        tag = new Tag();
        tag.setName("tag");
        tag.setId(1l);
        secondTag = new Tag();
        secondTag.setId(2l);
        secondTag.setName("23");
        listOfAll = new ArrayList<>();
        listOfAll.add(tag);
    }

//    @Test
//    public void create() {
//        when(tagDao.findTagByName(tag.getName())).thenReturn(null);
//        when(tagDao.create(tag);).thenReturn(tag);
//        Tag actualTag = service.create((tag));
//        assertTrue(actualTag.equals(tag));
//    }

    @Test
    public void createAlreadyExisting() {
        when(tagDao.findTagByName(tag.getName())).thenReturn(null);
        Tag actualTag = service.create((tag));
        assertTrue(actualTag.equals(tag));
    }

    @Test
    public void delete() {
        doNothing().when(tagDao).delete(tag);
        service.delete((tag));
        verify(tagDao).delete(tag);
    }

    @Test
    public void read() {
        listOfAll.add(secondTag);
        when(tagDao.read()).thenReturn(listOfAll);
        assertTrue(service.read().equals(listOfAll));
    }

    @Test
    public void findPaginated() {
        listOfAll.add(secondTag);
        when(tagDao.read()).thenReturn(listOfAll);
        assertTrue(service.findPaginated(1, 1).size() == 1);
        assertTrue(service.findPaginated(2, 1).size() == 2);
    }

    @Test
    public void readByName() {
        when(tagDao.findTagByName(tag.getName())).thenReturn(null);
        assertTrue(service.read(tag.getName()).equals(tag));
    }

    @Test
    public void readByNotExistingName() {
        when(tagDao.findTagByName("")).thenReturn(null);
        expectedException.expect(TagNotFoundException.class);
        service.read("");
    }

    @Test
    public void readByInvalidName() {
        expectedException.expect(InvalidParameterException.class);
        service.read(null);
    }

    @Test
    public void testReadNotValid() {
        expectedException.expect(InvalidParameterException.class);
        service.read(0);
    }

    @Test
    public void testReadNotExisting() {
        Optional<Tag> expected = Optional.ofNullable(null);
        when(tagDao.read(1l)).thenReturn(expected);
        expectedException.expect(TagNotFoundException.class);
        service.read(1);
    }

    @Test
    public void testReadById() {
        Optional<Tag> expected = Optional.ofNullable(tag);
        when(tagDao.read(1l)).thenReturn(expected);
        Tag actual = service.read(1);
        assertTrue(actual.equals(expected.get()));
    }

    @Test
    public void getTagsOfUserWithHighestPriceOfOrders() {
        List<Long> userIds = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        userIds.add(1l);
        when(tagDao.findMostUsedTagOfTopUser()).thenReturn(null);
        assertTrue(service.getTagsOfUserWithHighestPriceOfOrders().equals(tag));
    }
}