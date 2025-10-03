package com.example.controller;

import com.example.domain.entity.Order;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.mapper.OrderMapper;
import com.example.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController<Order, Long, OrderReqDTO, OrderResDTO> {

    private final OrderService orderService;

    protected OrderController(OrderService orderService, OrderMapper orderMapper) {
        super(orderService, orderMapper);
        this.orderService = orderService;
    }

}
