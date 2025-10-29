package com.example.service.impl;

import com.example.client.CinemaServiceClient;
import com.example.domain.entity.Ticket;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.MonthlyRevenueDTO;
import com.example.domain.response.MovieRevenueDTO;
import com.example.domain.response.TicketResDTO;
import com.example.repository.TicketRepository;
import com.example.service.TicketService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl
        extends BaseServiceImpl<Ticket, Long, TicketReqDTO, TicketResDTO>
        implements TicketService {

    private final TicketRepository ticketRepository;
    private final CinemaServiceClient cinemaServiceClient;

    protected TicketServiceImpl(TicketRepository ticketRepository,
                                CinemaServiceClient cinemaServiceClient) {
        super(ticketRepository);
        this.ticketRepository = ticketRepository;
        this.cinemaServiceClient = cinemaServiceClient;
    }

    @Override
    public List<Long> findLockedSeats(Long id, LocalDateTime time) {
        List<Ticket> tickets = ticketRepository.findLockedSeats(id, time);
        return tickets.stream()
                .map(Ticket::getSeatId)
                .toList();
    }

    @Override
    public Long getTicketsSoldToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1).minusSeconds(1);
        return ticketRepository.countTicketsSoldBetween(start, end);
    }

    @Override
    public Double getOccupancyRate() {
        Long soldSeats = ticketRepository.countTicketsSoldThisMonth();
        Long totalSeats = cinemaServiceClient.countActiveSeatsByMonth().getData();

        if (totalSeats == null || totalSeats == 0) return 0.0;

        return (soldSeats * 100.0) / totalSeats;
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenueChartData() {
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1; // Q1: 1-3, Q2:4-6,...

        // Xác định tháng đầu và tháng cuối của quý hiện tại
        int startMonth = (currentQuarter - 1) * 3 + 1;
        int endMonth = startMonth + 2;

        // 1️⃣ Lấy tất cả vé đã thanh toán
        List<Ticket> tickets = ticketRepository.findAllByPaidTrue();

        // 2️⃣ Lọc vé thuộc năm hiện tại và tháng trong quý
        List<Ticket> filteredTickets = tickets.stream()
                .filter(t -> {
                    int month = t.getCreatedAt().getMonthValue();
                    int year = t.getCreatedAt().getYear();
                    return year == currentYear && month >= startMonth && month <= endMonth;
                })
                .toList();

        // 3️⃣ Nhóm theo tháng
        Map<Integer, List<Ticket>> ticketsByMonth = filteredTickets.stream()
                .collect(Collectors.groupingBy(t -> t.getCreatedAt().getMonthValue()));

        // 4️⃣ Map sang DTO và sắp xếp
        return ticketsByMonth.entrySet().stream()
                .map(entry -> {
                    int month = entry.getKey();
                    double revenue = entry.getValue().stream()
                            .mapToDouble(Ticket::getPrice)
                            .sum();
                    long ticketCount = entry.getValue().size();
                    return new MonthlyRevenueDTO(month, revenue, ticketCount);
                })
                .sorted(Comparator.comparingInt(MonthlyRevenueDTO::getMonth))
                .toList();
    }

    @Override
    public List<MovieRevenueDTO> getTopMoviesCurrentQuarter(int topN) {
        // 1️⃣ Lấy quý hiện tại
        LocalDate now = LocalDate.now();
        int currentQuarter = now.get(IsoFields.QUARTER_OF_YEAR);
        int currentYear = now.getYear();

        // 2️⃣ Lấy tất cả vé đã thanh toán
        List<Ticket> tickets = ticketRepository.findAllByPaidTrue();

        // 3️⃣ Lọc vé theo quý hiện tại (dựa trên createdAt)
        List<Ticket> filteredTickets = tickets.stream()
                .filter(ticket -> {
                    LocalDate createdDate = ticket.getCreatedAt().toLocalDate();
                    int ticketQuarter = createdDate.get(IsoFields.QUARTER_OF_YEAR);
                    int ticketYear = createdDate.getYear();
                    return ticketQuarter == currentQuarter && ticketYear == currentYear;
                })
                .toList();

        // 4️⃣ Gom doanh thu theo showtimeId
        Map<Long, Double> showtimeToRevenue = filteredTickets.stream()
                .collect(Collectors.groupingBy(
                        Ticket::getShowtimeId,
                        Collectors.summingDouble(Ticket::getPrice)
                ));

        // 5️⃣ Gọi sang service khác để nhóm theo movie
        Map<String, Double> movieToRevenue = cinemaServiceClient
                .getTopMovie(showtimeToRevenue)
                .getData();

        // 6️⃣ Trả về top N phim có doanh thu cao nhất
        return movieToRevenue.entrySet().stream()
                .map(entry -> new MovieRevenueDTO(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingDouble(MovieRevenueDTO::getRevenue).reversed())
                .limit(topN)
                .toList();
    }
}
