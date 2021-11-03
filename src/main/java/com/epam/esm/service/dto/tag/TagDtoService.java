package com.epam.esm.service.dto.tag;

import com.epam.esm.mapper.tag.TagDtoMapper;
import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.entity.tag.TagService;
import com.epam.esm.service.entity.tag.TagServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagDtoService implements TagDtoServiceI {
    private TagDtoMapper mapper;
    private TagServiceI tagService;

    @Autowired
    public TagDtoService(TagDtoMapper mapper, TagService tagService) {
        this.mapper = mapper;
        this.tagService = tagService;
    }

    @Override
    public TagDto create(TagDto entity) {
        Tag tag = mapper.tagDtoToTag(entity);
        return mapper.tagToTagDto(tagService.create(tag));
    }

    @Override
    public void delete(TagDto entity) {
        Tag tag = mapper.tagDtoToTag(entity);
        tagService.delete(tag);
    }

    @Override
    public List<TagDto> read() {
        return tagListToTagDtoList(tagService.read());
    }

    @Override
    public TagDto read(long id) {
        return mapper.tagToTagDto(tagService.read(id));
    }

    @Override
    public TagDto read(String name) {
        return mapper.tagToTagDto(tagService.read(name));
    }

    @Override
    public List<TagDto> findPaginated(int page, int size) {
        List<TagDto> allTags = read();
        List<TagDto> paginatedTags = new ArrayList<>();
        try {
            paginatedTags = allTags
                    .subList(page * size, page * size + size);
        } catch (IndexOutOfBoundsException e) {
        }
        return paginatedTags;
    }

    private List<TagDto> tagListToTagDtoList(List<Tag> tagList) {
        return tagList.stream()
                .map((tag -> mapper.tagToTagDto(tag)))
                .collect(Collectors.toList());
    }
}
