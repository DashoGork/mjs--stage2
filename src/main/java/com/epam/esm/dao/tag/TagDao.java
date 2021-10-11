package com.epam.esm.dao.tag;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {
    void create(Tag tag);
    void delete(long id);
    Tag read(long id);
    Tag read(String name);
    List<Tag> read();
    void update(String name);
}