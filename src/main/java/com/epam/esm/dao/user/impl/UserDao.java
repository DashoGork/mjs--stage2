package com.epam.esm.dao.user.impl;

import com.epam.esm.dao.user.UserDaoI;
import com.epam.esm.enums.Queries;
import com.epam.esm.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class UserDao implements UserDaoI {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<User> read() {
        Query query =
                entityManager.createQuery(Queries.SELECT_ALL_USERS.getQuery());
        return query.getResultList();
    }

    @Override
    public List<User> read(int offset, int limit) {
        Query query =
                entityManager.createQuery(Queries.SELECT_ALL_USERS.getQuery())
                        .setFirstResult(offset).setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public Optional<User> read(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public void create(User user) {
        entityManager.persist(user);
    }
}
