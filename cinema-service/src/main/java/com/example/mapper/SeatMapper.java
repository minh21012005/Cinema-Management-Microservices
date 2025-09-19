package com.example.mapper;

import com.example.domain.entity.Seat;
import com.example.domain.request.ReqSeatDTO;
import com.example.domain.response.ResSeatDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper extends BaseMapper<Seat, ReqSeatDTO, ResSeatDTO> {
}
