package com.epam.esm.dao.order.impl;

import com.epam.esm.dao.order.OrderDaoI;
import com.epam.esm.enums.Queries;
import com.epam.esm.model.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@AllArgsConstructor
@Repository
public class OrderDao implements OrderDaoI {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        Query query =
                entityManager.createQuery(Queries.SELECT_ORDER_BY_USER_ID_AND_TIME_OFF_PURCHASE_AND_PRICE.getQuery());
        query.setParameter(1, order.getUserId());
        query.setParameter(2, order.getTimeOfPurchase());
        query.setParameter(3, order.getPrice());
        return (Order) query.getSingleResult();
    }
}
