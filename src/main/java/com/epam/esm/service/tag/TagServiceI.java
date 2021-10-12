package com.epam.esm.service.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.service.Service;

public interface TagServiceI extends Service<Tag> {
     Tag read(String name);
}