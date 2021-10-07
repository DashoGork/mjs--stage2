package com.epam.esm.service.tag;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.exceptions.TagNotFoundException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagService implements TagServiceI {
    private TagDao tagDao;

    @Autowired
    public TagService(TagDaoImplementation tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public void create(Tag tag) {
        tagDao.update(tag.getName());
    }

    @Override
    public void delete(Tag tag) {
        tagDao.delete(tag.getId());
    }

    @Override
    public List<Tag> read() {
        return tagDao.read();
    }

    @Override
    public Tag read(String name) {
        if (name != null) {
            try {
                return tagDao.read(name);
            } catch (TagNotFoundException e) {
                return new Tag();//null obj
            }
        } else {
            return new Tag();
        }
    }

    @Override
    public Tag read(long id) {
        try {
            return tagDao.read(id);
        } catch (TagNotFoundException e) {
            return new Tag();//null obj
        }
    }
}