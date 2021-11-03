package com.epam.esm.mapper.order;

import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.Order;

public interface OrderDtoMapper {
    Order orderDtoToOrder(OrderDto orderDto);

    OrderDto orderToOrderDto(Order order);
}
