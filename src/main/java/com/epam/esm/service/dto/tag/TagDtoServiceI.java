package com.epam.esm.service.dto.tag;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.DtoService;

public interface TagDtoServiceI extends DtoService<TagDto> {
    TagDto read(String name);

    TagDto getTagsOfUserWithHighestPriceOfOrders();
}