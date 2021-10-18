package com.epam.esm.service.tag;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.impl.TagDaoImplementation;
import com.epam.esm.mapper.tag.TagDtoMapper;
import com.epam.esm.mapper.tag.TagDtoMapperImplementation;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TagService implements TagServiceI {
    private TagDao tagDao;
    private TagDtoMapper mapper;

    @Autowired
    public TagService(TagDaoImplementation tagDao,
                      TagDtoMapperImplementation mapper) {
        this.tagDao = tagDao;
        this.mapper = mapper;
    }

    @Override
    public TagDto create(TagDto tag) {
        tagDao.update(tag.getName());
        return mapper.tagToTagDto(tagDao.read(tag.getName()));
    }

    @Override
    public void delete(TagDto tag) {
        tagDao.delete(tag.getId());
    }

    @Override
    public List<TagDto> read() {
        return tagListToTagDtoList(tagDao.read());
    }

    @Override
    public TagDto read(String name) {
        if (name != null) {
            return mapper.tagToTagDto(tagDao.read(name));
        } else {
            throw new InvalidParameterException("name is null");
        }
    }

    @Override
    public TagDto read(long id) {
        if (id > 0) {
            return mapper.tagToTagDto(tagDao.read(id));
        } else {
            throw new InvalidParameterException("invalid id. id = " + id);
        }
    }

    private List<TagDto> tagListToTagDtoList(List<Tag> tagList) {
        return tagList.stream()
                .map((tag -> mapper.tagToTagDto(tag)))
                .collect(Collectors.toList());
    }
}