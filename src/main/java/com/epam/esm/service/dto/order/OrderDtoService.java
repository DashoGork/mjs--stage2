package com.epam.esm.service.dto.order;

import com.epam.esm.mapper.order.OrderDtoMapper;
import com.epam.esm.mapper.order.OrderDtoMapperImpl;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.service.entity.order.OrderService;
import com.epam.esm.service.entity.order.OrderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoService implements OrderDtoServiceI {
    private OrderDtoMapper mapper;
    private OrderServiceI service;

    @Autowired
    public OrderDtoService(OrderDtoMapperImpl mapper, OrderService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        Order order = mapper.orderDtoToOrder(orderDto);
        return mapper.orderToOrderDto(service.addOrder(order));
    }
}
