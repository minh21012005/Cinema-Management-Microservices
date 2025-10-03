package com.example.service.impl;

import com.example.domain.entity.Order;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl
        extends BaseServiceImpl<Order, Long, OrderReqDTO, OrderResDTO>
        implements OrderService {

    private final OrderRepository orderRepository;

    protected OrderServiceImpl(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }
}
