package com.epam.esm.service.entity.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.service.entity.Service;

public interface TagServiceI extends Service<Tag> {
    Tag read(String name);
}