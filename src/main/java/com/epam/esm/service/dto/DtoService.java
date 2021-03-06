package com.epam.esm.service.dto;

import java.util.List;

public interface DtoService<BaseEntityDto> {
    BaseEntityDto create(BaseEntityDto entity);
    void delete(BaseEntityDto entity);
    List<BaseEntityDto> read();
    List<BaseEntityDto> findPaginated(int page, int size);
    BaseEntityDto read(long id);
}