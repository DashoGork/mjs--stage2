package com.epam.esm.service.dto.tag;

import com.epam.esm.model.dto.CertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.dto.DtoService;
import com.epam.esm.service.entity.Service;

import java.util.List;

public interface TagDtoServiceI extends DtoService<TagDto> {
    TagDto read(String name);

}
