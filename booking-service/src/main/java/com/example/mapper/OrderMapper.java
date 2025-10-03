package com.example.mapper;

import com.example.domain.entity.Order;
import com.example.domain.entity.OrderItem;
import com.example.domain.entity.Ticket;
import com.example.domain.response.OrderResDTO;
import com.example.domain.request.OrderReqDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper extends BaseMapper<Order, OrderReqDTO, OrderResDTO> {

    @Override
    @Mapping(target = "tickets", source = "tickets", qualifiedByName = "mapTickets")
    @Mapping(target = "items", source = "items", qualifiedByName = "mapItems")
    OrderResDTO toDto(Order entity);

    @Override
    Order toEntity(OrderReqDTO dto);

    @Override
    void updateEntityFromDto(OrderReqDTO dto, @MappingTarget Order entity);

    @Named("mapTickets")
    default List<OrderResDTO.TicketRes> mapTickets(List<Ticket> tickets) {
        if (tickets == null) return null;
        return tickets.stream().map(t -> OrderResDTO.TicketRes.builder()
                        .ticketId(t.getId())
                        .seatId(t.getSeatId())
                        .price(t.getPrice())
                        .paid(t.isPaid())
                        .reserved(t.isReserved())
                        .build())
                .collect(Collectors.toList());
    }

    @Named("mapItems")
    default List<OrderResDTO.ItemRes> mapItems(List<OrderItem> items) {
        if (items == null) return null;
        return items.stream().map(i -> OrderResDTO.ItemRes.builder()
                        .itemId(i.getId())
                        .foodId(i.getFoodId())
                        .comboId(i.getComboId())
                        .quantity(i.getQuantity())
                        .price(i.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
