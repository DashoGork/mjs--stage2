package com.epam.esm.dao.tag;

import com.epam.esm.dao.Dao;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDaoI extends Dao<Tag> {
    Optional<Tag> findTagByName(String name);

    List<Tag> findTagsByCertificates(Certificate certificate);

    String findMostUsedTagOfTopUser();
}
