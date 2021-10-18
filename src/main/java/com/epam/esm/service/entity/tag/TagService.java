package com.epam.esm.service.entity.tag;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.mapper.tag.TagDtoMapperImplementation;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;


@Service
public class TagService implements TagServiceI {
    private TagDao tagDao;

    @Autowired
    public TagService(TagDaoImplementation tagDao,
                      TagDtoMapperImplementation mapper) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag create(Tag tag) {
        tagDao.update(tag.getName());
        return tagDao.read(tag.getName());
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
            return tagDao.read(name);
        } else {
            throw new InvalidParameterException("name is null");
        }
    }

    @Override
    public Tag read(long id) {
        if (id > 0) {
            return tagDao.read(id);
        } else {
            throw new InvalidParameterException("invalid id. id = " + id);
        }
    }


}