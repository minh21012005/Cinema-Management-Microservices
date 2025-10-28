package com.example.service.impl;

import com.example.client.CinemaServiceClient;
import com.example.domain.entity.Ticket;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.TicketResDTO;
import com.example.repository.TicketRepository;
import com.example.service.TicketService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
