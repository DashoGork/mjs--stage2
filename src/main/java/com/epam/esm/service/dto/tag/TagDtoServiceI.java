package com.epam.esm.service.dto.tag;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.entity.Service;

public interface TagDtoServiceI extends Service<TagDto> {
    TagDto read(String name);
}
