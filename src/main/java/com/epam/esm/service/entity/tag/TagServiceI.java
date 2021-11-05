package com.epam.esm.service.entity.tag;

import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.entity.Service;

import java.util.List;

public interface TagServiceI extends Service<Tag> {
    Tag read(String name);

    List<Tag> findPaginated(int size, int page);
}