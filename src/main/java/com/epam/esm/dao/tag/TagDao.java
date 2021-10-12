package com.epam.esm.dao.tag;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.Tag;

public interface TagDao extends Dao<Tag> {
    Tag read(String name);
    void update(String name);
}