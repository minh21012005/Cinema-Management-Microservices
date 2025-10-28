package com.example.service.impl;

import com.example.client.CinemaServiceClient;
import com.example.domain.entity.Order;
import com.example.domain.entity.OrderItem;
import com.example.domain.entity.Payment;
import com.example.domain.entity.Ticket;
import com.example.domain.enums.PaymentMethod;
import com.example.domain.enums.PaymentStatus;
import com.example.domain.request.BookingRequest;
import com.example.domain.request.OrderReqDTO;
import com.example.domain.response.OrderResDTO;
import com.example.mapper.OrderMapper;
import com.example.repository.OrderRepository;
import com.example.repository.PaymentRepository;
import com.example.repository.TicketRepository;
import com.example.service.OrderService;
import com.example.service.PaymentService;
import com.example.util.error.IdInvalidException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl
        extends BaseServiceImpl<Order, Long, OrderReqDTO, OrderResDTO>
        implements OrderService {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final OrderMapper orderMapper;
    private final CinemaServiceClient cinemaServiceClient;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(TicketRepository ticketRepository,
                            OrderRepository orderRepository,
                            OrderMapper orderMapper,
                            CinemaServiceClient cinemaServiceClient,
                            PaymentRepository paymentRepository,
                            PaymentService paymentService,
                            SimpMessagingTemplate simpMessagingTemplate) {
        super(orderRepository);
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.cinemaServiceClient = cinemaServiceClient;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.messagingTemplate = simpMessagingTemplate;
    }

    @Transactional
    public OrderResDTO createOrder(OrderReqDTO request) throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Kiểm tra xuất chiếu đã xong chưa
        boolean isShowtimeEnd = cinemaServiceClient.isShowtimeEnd(request.getShowtimeId());
        if (isShowtimeEnd) {
            throw new IdInvalidException("Xuất chiếu đã kết thúc, không thể đặt vé!");
        }

        // 1. Kiểm tra ghế có còn trống không
        LocalDateTime now = LocalDateTime.now();
        for (BookingRequest.SeatDTO seatReq : request.getSeats()) {
            boolean exists = ticketRepository.existsBySeatIdAndShowtimeIdAndPaidTrueOrReservedTrue(
                    seatReq.getSeatId(), request.getShowtimeId(), now.minusMinutes(5));
            if (exists) {
                throw new IdInvalidException("Seat " + seatReq.getSeatId() + " đã được đặt!");
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

        if (request.getCustomerName() != null && !request.getCustomerPhone().isEmpty()) {
            order.setCustomerName(request.getCustomerName());
        }
        if (request.getCustomerPhone() != null && !request.getCustomerPhone().isEmpty()) {
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

        // 7. Tạo payment tương ứng
        Payment payment = Payment.builder()
                .order(saved)
                .method(request.getPaymentMethod())
                .amount(total)
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        // 8. Publish WebSocket event thông báo ghế đã đặt
        List<Long> bookedSeats = tickets.stream().map(Ticket::getSeatId).toList();
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "BOOKED");
        payload.put("seatIds", bookedSeats);
        messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(), payload);

        // 9. Nếu thanh toán COD → trả về luôn Order
        if (request.getPaymentMethod() == PaymentMethod.CASH) {
            paymentService.confirmCodPayment(payment);
        }

        return orderMapper.toDto(saved);
    }

    @Override
    public OrderResDTO booking(OrderReqDTO request) throws IdInvalidException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Kiểm tra xuất chiếu đã xong chưa
        boolean isShowtimeEnd = cinemaServiceClient.isShowtimeEnd(request.getShowtimeId());
        if (isShowtimeEnd) {
            throw new IdInvalidException("Xuất chiếu đã kết thúc, không thể đặt vé!");
        }

        // 1. Kiểm tra ghế có còn trống không
        LocalDateTime now = LocalDateTime.now();
        for (BookingRequest.SeatDTO seatReq : request.getSeats()) {
            boolean exists = ticketRepository.existsBySeatIdAndShowtimeIdAndPaidTrueOrReservedTrue(
                    seatReq.getSeatId(), request.getShowtimeId(), now.minusMinutes(5));
            if (exists) {
                throw new IdInvalidException("Seat " + seatReq.getSeatId() + " đã được đặt!");
            }
        }

        // 2. Tính tổng tiền
        double total = 0.0;
        total += request.getSeats().stream().mapToDouble(BookingRequest.SeatDTO::getPrice).sum();
        total += request.getFoods().stream().mapToDouble(f -> f.getPrice() * f.getQuantity()).sum();
        total += request.getCombos().stream().mapToDouble(c -> c.getPrice() * c.getQuantity()).sum();

        Long userId = Long.valueOf(authentication.getName());

        // 3. Tạo Order
        Order order = Order.builder()
                .userId(userId)
                .totalAmount(total)
                .paid(false) // ban đầu chưa thanh toán
                .build();

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

        // 7. Tạo payment tương ứng
        Payment payment = Payment.builder()
                .order(saved)
                .method(request.getPaymentMethod())
                .amount(total)
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        // 8. Publish WebSocket event thông báo ghế đã đặt
        List<Long> bookedSeats = tickets.stream().map(Ticket::getSeatId).toList();
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "BOOKED");
        payload.put("seatIds", bookedSeats);
        messagingTemplate.convertAndSend("/topic/seats/" + request.getShowtimeId(), payload);
        return orderMapper.toDto(saved);
    }

    @Override
    public void cancel(Long id) throws IdInvalidException {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Order không tồn tại trong hệ thống!")
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName()); //ID của staff hoặc customer

        if (order.getUserId() != null) {
            if (!Objects.equals(order.getUserId(), userId)) {
                throw new IdInvalidException("Không có quyền hủy order này!");
            }
        } else {
            if (!Objects.equals(order.getStaffId(), userId)) {
                throw new IdInvalidException("Không có quyền hủy order này!");
            }
        }

        // 🔹 Lấy danh sách ghế của order
        List<Long> seatIds = order.getTickets()
                .stream()
                .map(Ticket::getSeatId)
                .toList();

        // 🔹 Lấy showtimeId để biết kênh WebSocket cần gửi
        Long showtimeId = order.getTickets().isEmpty()
                ? null
                : order.getTickets().getFirst().getShowtimeId();

        orderRepository.delete(order);

        if (showtimeId != null && !seatIds.isEmpty()) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("type", "RELEASED");
            payload.put("seatIds", seatIds);
            messagingTemplate.convertAndSend("/topic/seats/" + showtimeId, payload);

        }
    }

    /**
     * ✅ Tính tổng doanh thu trong tháng được chỉ định (VD: 2025-10)
     */
    public Double getRevenueByMonth(int year, int month) {
        LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        return orderRepository.getTotalRevenueBetween(start, end);
    }

    /**
     * ✅ Tính doanh thu trong một ngày cụ thể
     */
    @Override
    public Double getRevenueByDay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusSeconds(1);
        return orderRepository.getTotalRevenueBetween(start, end);
    }

    /**
     * ✅ Tính tổng doanh thu từ đầu năm đến hiện tại
     */
    public Double getRevenueFromYearStart() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfYear = LocalDate.of(today.getYear(), 1, 1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        return orderRepository.getTotalRevenueBetween(startOfYear, now);
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
