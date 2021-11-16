package com.epam.esm.service.dto.order;

import com.epam.esm.model.dto.OrderDto;

public interface OrderDtoServiceI {
    OrderDto create(OrderDto orderDto);
}