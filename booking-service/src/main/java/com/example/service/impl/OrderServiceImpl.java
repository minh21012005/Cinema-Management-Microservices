package com.example.service.impl;

import com.example.domain.entity.Order;
import com.example.domain.entity.OrderItem;
import com.example.domain.entity.Ticket;
import com.example.domain.request.BookingRequest;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.mapper.OrderMapper;
import com.example.repository.OrderRepository;
import com.example.repository.TicketRepository;
import com.example.service.OrderService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl
        extends BaseServiceImpl<Order, Long, OrderReqDTO, OrderResDTO>
        implements OrderService {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(TicketRepository ticketRepository,
                              OrderRepository orderRepository,
                              OrderMapper orderMapper,
                              SimpMessagingTemplate simpMessagingTemplate) {
        super(orderRepository);
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @Transactional
    public OrderResDTO createOrder(OrderReqDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 1. Kiểm tra ghế có còn trống không
        LocalDateTime now = LocalDateTime.now();
        for (BookingRequest.SeatDTO seatReq : request.getSeats()) {
            boolean exists = ticketRepository.existsBySeatIdAndShowtimeIdAndPaidTrueOrReservedTrue(
                    seatReq.getSeatId(), request.getShowtimeId(), now.minusMinutes(5));
            if (exists) {
                throw new RuntimeException("Seat " + seatReq.getSeatId() + " đã được đặt!");
            }
        }

        // 2. Tính tổng tiền
        double total = 0.0;
        total += request.getSeats().stream().mapToDouble(BookingRequest.SeatDTO::getPrice).sum();
        total += request.getFoods().stream().mapToDouble(f -> f.getPrice() * f.getQuantity()).sum();
        total += request.getCombos().stream().mapToDouble(c -> c.getPrice() * c.getQuantity()).sum();

        Long staffId = Long.valueOf(authentication.getName());

        // 3. Tạo Order
        Order order = Order.builder()
                .staffId(staffId)
                .totalAmount(total)
                .paid(false) // ban đầu chưa thanh toán
                .build();

        if(request.getCustomerName()!= null && !request.getCustomerPhone().isEmpty()){
            order.setCustomerName(request.getCustomerName());
        }
        if(request.getCustomerPhone()!= null && !request.getCustomerPhone().isEmpty()){
            order.setCustomerPhone(request.getCustomerPhone());
        }

        // 4. Thêm Ticket
        List<Ticket> tickets = request.getSeats().stream()
                .map(seatReq -> Ticket.builder()
                        .seatId(seatReq.getSeatId())
                        .showtimeId(request.getShowtimeId())
                        .price(seatReq.getPrice())
                        .paid(false)
                        .reserved(true)
                        .reservedAt(LocalDateTime.now())
                        .order(order)
                        .build())
                .toList();
        order.setTickets(tickets);

        // 5. Thêm OrderItem (food/combo)
        List<OrderItem> items = new ArrayList<>();
        request.getFoods().forEach(f -> items.add(
                OrderItem.builder()
                        .foodId(f.getFoodId())
                        .quantity(f.getQuantity())
                        .price(f.getPrice() * f.getQuantity())
                        .order(order)
                        .build()
        ));
        request.getCombos().forEach(c -> items.add(
                OrderItem.builder()
                        .comboId(c.getComboId())
                        .quantity(c.getQuantity())
                        .price(c.getPrice() * c.getQuantity())
                        .order(order)
                        .build()
        ));
        order.setItems(items);

        // 6. Lưu Order + Ticket + Item
        Order saved = orderRepository.save(order);

        // 7. Publish WebSocket event thông báo ghế đã đặt
        List<Long> bookedSeats = tickets.stream().map(Ticket::getSeatId).toList();
        messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(), bookedSeats);

        return orderMapper.toDto(saved);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredSeats() {
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(5);
        List<Ticket> expiredSeats = ticketRepository.findByReservedTrueAndReservedAtBefore(expiredTime);
        expiredSeats.forEach(seat -> seat.setReserved(false));
        ticketRepository.saveAll(expiredSeats);
    }
}
