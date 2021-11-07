package com.epam.esm.service.dto.order;

import com.epam.esm.mapper.order.OrderDtoMapperImpl;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.service.entity.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderDtoServiceTest {
    @Mock
    private OrderDtoMapperImpl mapper;
    @Mock
    private OrderService service;

    private OrderDtoService orderDtoService;
    private Order order;
    private OrderDto orderDto;

    @Before
    public void setUp() throws Exception {
        orderDtoService = new OrderDtoService(mapper, service);
        order = new Order();
        order.setPrice(12);
        order.setId(1l);
        orderDto = new OrderDto();
        orderDto.setPrice(12);
        orderDto.setId(1l);
    }

    @Test
    public void create() {
        when(mapper.orderDtoToOrder(orderDto)).thenReturn(order);
        when(service.addOrder(order)).thenReturn(order);
        when(mapper.orderToOrderDto(order)).thenReturn(orderDto);
        assertTrue(orderDtoService.create(orderDto).equals(orderDto));
        verify(service).addOrder(order);
    }
}