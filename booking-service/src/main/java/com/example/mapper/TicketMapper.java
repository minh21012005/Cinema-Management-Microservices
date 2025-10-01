package com.example.mapper;

import com.example.domain.entity.Ticket;
import com.example.domain.request.TicketReqDTO;
import com.example.domain.response.TicketResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper extends BaseMapper<Ticket, TicketReqDTO, TicketResDTO> {
}
