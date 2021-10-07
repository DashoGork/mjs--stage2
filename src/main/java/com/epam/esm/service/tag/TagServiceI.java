package com.epam.esm.service.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.service.Service;

import java.util.List;

public interface TagServiceI extends Service<Tag> {
     void create(Tag tag);
     void delete(Tag tag);
     List<Tag> read();
     Tag read(String name);
     Tag read(long id);
}
