package com.example.service.impl;

import com.example.domain.entity.Ticket;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.TicketResDTO;
import com.example.repository.TicketRepository;
import com.example.service.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl
        extends BaseServiceImpl<Ticket, Long, TicketReqDTO, TicketResDTO>
        implements TicketService {

    private final TicketRepository ticketRepository;

    protected TicketServiceImpl(TicketRepository ticketRepository) {
        super(ticketRepository);
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Long> findByShowtimeIdAndPaidTrue(Long id) {
        List<Ticket> tickets = ticketRepository.findByShowtimeIdAndPaidTrue(id);
        return tickets.stream()
                .map(Ticket::getSeatId)
                .toList();
    }
}
