package com.epam.esm.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<BaseEntity> {
    void create(BaseEntity entity);

    List<BaseEntity> read(int offset, int limit);

    List<BaseEntity> read();

    Optional<BaseEntity> read(long id);

    void delete(BaseEntity baseEntity);
}