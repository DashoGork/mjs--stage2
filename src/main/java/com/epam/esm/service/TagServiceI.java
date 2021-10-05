package com.epam.esm.service;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagServiceI extends Service<Tag>{
    public void create(Tag tag);
    public void delete(Tag tag);
    public List<Tag> read();
    public Tag read(String name);
    public Tag read(long id);
}
