package com.example.service;

import com.example.domain.entity.Ticket;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.TicketResDTO;

import java.util.List;

public interface TicketService extends BaseService<Ticket, Long, TicketReqDTO, TicketResDTO> {
    List<Long> findByShowtimeIdAndPaidTrue(Long id);
}
