package com.example.service;

import com.example.domain.entity.Order;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;

public interface OrderService extends BaseService<Order, Long, OrderReqDTO, OrderResDTO> {
}
