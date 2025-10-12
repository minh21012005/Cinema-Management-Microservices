package com.example.controller;

import com.example.domain.entity.Order;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.mapper.OrderMapper;
import com.example.service.OrderService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController extends BaseController<Order, Long, OrderReqDTO, OrderResDTO> {

    private final OrderService orderService;

    protected OrderController(OrderService orderService, OrderMapper orderMapper) {
        super(orderService, orderMapper);
        this.orderService = orderService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ORDER_CREATE')")
    public ResponseEntity<OrderResDTO> create(@RequestBody OrderReqDTO dto) throws IdInvalidException {
        OrderResDTO order = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PreAuthorize("hasPermission(null, 'BOOKING_CREATED')")
    @PostMapping("/booking")
    public ResponseEntity<OrderResDTO> booking(@RequestBody OrderReqDTO dto) throws IdInvalidException {
        OrderResDTO order = orderService.booking(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @Override
    @PreAuthorize("hasPermission(null, 'BOOKING_CANCEL')")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        orderService.cancel(id);
        return ResponseEntity.ok(null);
    }

}
