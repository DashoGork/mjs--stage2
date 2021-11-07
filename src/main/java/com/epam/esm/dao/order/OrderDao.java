package com.epam.esm.dao.order;

import com.epam.esm.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {
    @Query("select userId from Order where userId is not null group by userId " +
            "order " +
            "by " +
            "sum(price) desc ")
    List<Long> findTopUserByPrice();
}