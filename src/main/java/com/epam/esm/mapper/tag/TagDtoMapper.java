package com.epam.esm.mapper.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.TagDto;
import org.mapstruct.Mapper;

@Mapper
public interface TagDtoMapper {
    Tag tagDtoToTag(TagDto tagDto);

    TagDto tagToTagDto(Tag tag);
}