package com.epam.esm.mapper.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapperImplementation implements TagDtoMapper {
    @Override
    public Tag tagDtoToTag(TagDto tagDto) {
        if (tagDto == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(tagDto.getName());
        tag.setId(tagDto.getId());
        return tag;
    }

    @Override
    public TagDto tagToTagDto(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagDto tagDto = new TagDto();
        tagDto.setName(tag.getName());
        tagDto.setId(tag.getId());
        return tagDto;
    }
}
