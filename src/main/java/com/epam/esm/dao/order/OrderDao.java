package com.epam.esm.dao.order;

import com.epam.esm.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {
}
