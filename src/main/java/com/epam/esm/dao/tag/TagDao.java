package com.epam.esm.dao.tag;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao extends Dao<Tag> {
    Tag read(String name);
    void update(String name);
}