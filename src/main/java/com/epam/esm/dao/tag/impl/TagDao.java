package com.epam.esm.dao.tag.impl;

import com.epam.esm.dao.tag.TagDaoI;
import com.epam.esm.enums.Queries;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class TagDao implements TagDaoI {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void create(Tag tag) {
        entityManager.persist(tag);
    }

    @Override
    public List<Tag> read() {
        Query query =
                entityManager.createQuery(Queries.SELECT_ALL_TAGS.getQuery());
        return query.getResultList();
    }

    @Override
    public List<Tag> read(int offset, int limit) {
        Query query =
                entityManager.createQuery(Queries.SELECT_ALL_TAGS.getQuery())
                        .setFirstResult(offset).setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public Optional<Tag> read(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public void delete(Tag tag) {
        Query query =
                entityManager.createQuery(Queries.DELETE_TAG_BY_ID.getQuery());
        query.setParameter(1, tag.getId());
        query.executeUpdate();
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        Optional<Tag> tag;
        try {
            Query query =
                    entityManager.createQuery(Queries.SELECT_TAG_BY_NAME.getQuery());
            query.setParameter(1, name);
            tag = Optional.ofNullable((Tag) query.getSingleResult());
        } catch (NoResultException ex) {
            tag = Optional.empty();
        }
        return tag;
    }

    @Override
    public List<Tag> findTagsByCertificates(Certificate certificate) {
        return null;
    }

    @Override
    public String findMostUsedTagOfTopUser() {
        Query query =
                entityManager.createNativeQuery(Queries.SELECT_TOP_TAG.getQuery(), Tag.class).setMaxResults(1);
        Tag t = (Tag) query.getResultList().get(0);
        return t.getName();
    }
}
