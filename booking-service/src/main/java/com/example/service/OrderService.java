package com.example.service;

import com.example.domain.entity.Order;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.util.error.IdInvalidException;

public interface OrderService extends BaseService<Order, Long, OrderReqDTO, OrderResDTO>{
    OrderResDTO createOrder(OrderReqDTO request) throws IdInvalidException;
    OrderResDTO booking(OrderReqDTO request) throws IdInvalidException;
    void cancel(Long id) throws IdInvalidException;
}
