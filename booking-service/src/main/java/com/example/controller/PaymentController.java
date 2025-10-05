package com.example.controller;

import com.example.domain.entity.Payment;
import com.example.domain.entity.Ticket;
import com.example.domain.request.PaymentReqDTO;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.PaymentResDTO;
import com.example.domain.response.TicketResDTO;
import com.example.mapper.PaymentMapper;
import com.example.mapper.TicketMapper;
import com.example.service.PaymentService;
import com.example.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController extends BaseController<Payment, Long, PaymentReqDTO, PaymentResDTO> {

    private final PaymentService paymentService;

    protected PaymentController(PaymentService paymentService, PaymentMapper paymentMapper) {
        super(paymentService, paymentMapper);
        this.paymentService = paymentService;
    }

}
