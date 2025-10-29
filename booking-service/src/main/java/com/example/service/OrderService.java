package com.example.service;

import com.example.domain.entity.Order;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.domain.response.TopUserDTO;
import com.example.domain.response.TransactionResDTO;
import com.example.util.error.IdInvalidException;

import java.time.LocalDate;
import java.util.List;

public interface OrderService extends BaseService<Order, Long, OrderReqDTO, OrderResDTO>{
    OrderResDTO createOrder(OrderReqDTO request) throws IdInvalidException;
    OrderResDTO booking(OrderReqDTO request) throws IdInvalidException;
    void cancel(Long id) throws IdInvalidException;
    Double getRevenueByDay(LocalDate date);
    Double getRevenueByMonth(int year, int month);
    List<TopUserDTO> getTopCustomers(int topN);
    List<TransactionResDTO> getOrdersByDate(LocalDate date);
}
