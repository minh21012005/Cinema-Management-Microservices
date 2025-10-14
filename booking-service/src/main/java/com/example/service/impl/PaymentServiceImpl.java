package com.example.service.impl;

import com.example.client.AuthClient;
import com.example.client.ShowtimeClient;
import com.example.domain.entity.*;
import com.example.domain.enums.PaymentStatus;
import com.example.domain.request.PaymentReqDTO;
import com.example.domain.request.SepayWebhookReqDTO;
import com.example.domain.request.TicketDataRequest;
import com.example.domain.response.OrderResDTO;
import com.example.domain.response.PaymentResDTO;
import com.example.mapper.OrderMapper;
import com.example.mapper.PaymentMapper;
import com.example.mapper.SepayMapper;
import com.example.repository.OrderRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.SepayRepository;
import com.example.service.EmailService;
import com.example.service.PaymentService;
import com.example.util.error.IdInvalidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private final SimpMessagingTemplate messagingTemplate;
    private final ShowtimeClient showtimeClient;
    private final AuthClient authClient;
    private final EmailService emailService;

    protected PaymentServiceImpl(PaymentRepository paymentRepository,
                                 OrderRepository orderRepository,
                                 PaymentMapper paymentMapper,
                                 SepayMapper sepayMapper,
                                 OrderMapper orderMapper,
                                 SepayRepository sepayRepository,
                                 ShowtimeClient showtimeClient,
                                 AuthClient authClient,
                                 EmailService emailService,
                                 SimpMessagingTemplate messagingTemplate) {
        super(paymentRepository);
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentMapper = paymentMapper;
        this.sepayMapper = sepayMapper;
        this.orderMapper = orderMapper;
        this.sepayRepository = sepayRepository;
        this.showtimeClient = showtimeClient;
        this.authClient = authClient;
        this.emailService = emailService;
        this.messagingTemplate = messagingTemplate;
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
        OrderResDTO dto = orderMapper.toDto(orderRepository.save(order));
        messagingTemplate.convertAndSend("/topic/order-status/" + order.getId(), dto);

        if(order.getUserId() != null){
            String email = authClient.fetchEmailById(order.getUserId());
            this.sendTicketEmail(order, email);
        }

        return dto;
    }

    private void sendTicketEmail(Order order, String email) {
        try {
            List<Ticket> tickets = order.getTickets();
            if (tickets == null || tickets.isEmpty()) return;

            Long showtimeId = tickets.getFirst().getShowtimeId();
            List<Long> seatIds = order.getTickets().stream().map(Ticket::getSeatId).toList();
            List<Long> foodIds = order.getItems().stream()
                    .filter(i -> i.getFoodId() != null)
                    .map(OrderItem::getFoodId)
                    .toList();
            List<Long> comboIds = order.getItems().stream()
                    .filter(i -> i.getComboId() != null)
                    .map(OrderItem::getComboId)
                    .toList();

            TicketDataRequest request = new TicketDataRequest();
            request.setSeatIds(seatIds);
            request.setFoodIds(foodIds);
            request.setComboIds(comboIds);

            TicketEmailDTO ticketData = showtimeClient.fetchTicketData(showtimeId, request).getData();
            ticketData.setTotalPrice(order.getTotalAmount());

            // ✅ Gửi mail
            emailService.sendTicketEmail(email, ticketData);

        } catch (Exception e) {
            System.err.println("❌ Lỗi khi gửi email vé: " + e.getMessage());
        }
    }
}
