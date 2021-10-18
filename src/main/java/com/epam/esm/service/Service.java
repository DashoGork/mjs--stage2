package com.epam.esm.service;

import java.util.List;

public interface Service<BaseEntityDto> {
    BaseEntityDto create(BaseEntityDto entity);

    void delete(BaseEntityDto entity);

    List<BaseEntityDto> read();

    BaseEntityDto read(long id);
}