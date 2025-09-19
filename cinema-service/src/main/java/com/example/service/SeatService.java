package com.example.service;

import com.example.domain.entity.Seat;
import com.example.domain.entity.SeatType;
import com.example.domain.request.ReqSeatDTO;
import com.example.domain.response.ResSeatDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface SeatService extends BaseService<Seat, Long, ReqSeatDTO, ResSeatDTO> {
    ResSeatDTO createSeat(ReqSeatDTO dto) throws IdInvalidException;
    List<SeatType> fetchAllSeatTypes();
    List<ResSeatDTO> fetchSeatsByRoom(Long id) throws IdInvalidException;
}
