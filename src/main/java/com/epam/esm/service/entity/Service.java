package com.epam.esm.service.entity;

import java.util.List;

public interface Service<BaseEntity> {
    BaseEntity create(BaseEntity entity);

    void delete(BaseEntity entity);

    List<BaseEntity> read();

    BaseEntity read(long id);
}