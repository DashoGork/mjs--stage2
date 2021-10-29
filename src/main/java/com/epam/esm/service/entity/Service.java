package com.epam.esm.service.entity;

import java.util.List;
import java.util.Optional;

public interface Service<BaseEntity> {
    BaseEntity create(BaseEntity entity);

    void delete(BaseEntity entity);

    List<BaseEntity> read();

    BaseEntity read(long id);
}