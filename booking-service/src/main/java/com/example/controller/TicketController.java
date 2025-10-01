package com.example.controller;

import com.example.domain.entity.Ticket;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.TicketResDTO;
import com.example.mapper.TicketMapper;
import com.example.service.TicketService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController extends BaseController<Ticket, Long, TicketReqDTO, TicketResDTO> {

    private final TicketService ticketService;

    protected TicketController(TicketService ticketService, TicketMapper ticketMapper) {
        super(ticketService, ticketMapper);
        this.ticketService = ticketService;
    }

    @GetMapping("/showtime/{id}/booked-seats")
    @PreAuthorize("hasPermission(null, 'SEAT_VIEW')")
    public List<Long> getBookedSeats(@PathVariable("id") Long id) {
        return ticketService.findByShowtimeIdAndPaidTrue(id);
    }
}
