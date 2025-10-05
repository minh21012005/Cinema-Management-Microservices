package com.example.service.impl;

import com.example.domain.entity.Order;
import com.example.domain.entity.Payment;
import com.example.domain.entity.SePayTransaction;
import com.example.domain.entity.Ticket;
import com.example.domain.enums.PaymentStatus;
import com.example.domain.request.PaymentReqDTO;
import com.example.domain.request.SepayWebhookReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.domain.response.PaymentResDTO;
import com.example.mapper.OrderMapper;
import com.example.mapper.PaymentMapper;
import com.example.mapper.SepayMapper;
import com.example.repository.OrderRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.SepayRepository;
import com.example.service.PaymentService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentServiceImpl
        extends BaseServiceImpl<Payment, Long, PaymentReqDTO, PaymentResDTO>
        implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    private final SepayMapper sepayMapper;
    private final OrderMapper orderMapper;
    private final SepayRepository sepayRepository;

    protected PaymentServiceImpl(PaymentRepository paymentRepository,
                                 OrderRepository orderRepository,
                                 PaymentMapper paymentMapper,
                                 SepayMapper sepayMapper,
                                 OrderMapper orderMapper,
                                 SepayRepository sepayRepository) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.sepayMapper = sepayMapper;
        this.orderMapper = orderMapper;
        this.sepayRepository = sepayRepository;
    }

    @Override
    public PaymentResDTO confirmCodPayment(Payment payment) {
        Order order = payment.getOrder();
        order.setPaid(true);

        List<Ticket> tickets = order.getTickets();
        for (Ticket ticket : tickets) {
            ticket.setPaid(true);
            ticket.setReserved(false);
            ticket.setReservedAt(null);
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Transactional
    @Override
    public OrderResDTO confirmSepayPayment(SepayWebhookReqDTO payload) throws IdInvalidException {

        // Chuyển payload thành entity
        SePayTransaction sePayTransaction = sepayMapper.toEntity(payload);

        // Lấy orderId từ content
        String[] parts = payload.getContent().split(" ");
        Long orderId = Long.valueOf(parts[0]);

        // Lấy Order
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new IdInvalidException("Order không hợp lệ!")
        );

        // Cập nhật trạng thái Order
        order.setPaid(true);
        sePayTransaction.setStatus("SUCCESS");
        sePayTransaction.setOrder(order);

        // Lưu SePayTransaction trước
        sePayTransaction = sepayRepository.save(sePayTransaction);

        // Cập nhật tickets
        if (order.getTickets() != null) {
            for (Ticket ticket : order.getTickets()) {
                ticket.setPaid(true);
                ticket.setReserved(false);
                ticket.setReservedAt(null);
                ticket.setOrder(order);
            }
        }

        // Cập nhật payments
        if (order.getPayments() != null) {
            for (Payment payment : order.getPayments()) {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setOrder(order); // chắc chắn gán order
                payment.setSePayTransaction(sePayTransaction); // đã persist trước
            }
        }

        // Lưu Order
        return orderMapper.toDto(orderRepository.save(order));
    }
}
