package com.epam.esm.service;

import java.util.List;

public interface Service<BaseEntity> {
    void create(BaseEntity entity);
    void delete(BaseEntity entity);
    List<BaseEntity> read();
    BaseEntity read(long id);
}