package com.epam.esm.dao;

import java.util.List;

public interface Dao<BaseEntity> {
    void create(BaseEntity entity);
    List<BaseEntity> read();
    BaseEntity read(long id);
    void delete(long id);
}
