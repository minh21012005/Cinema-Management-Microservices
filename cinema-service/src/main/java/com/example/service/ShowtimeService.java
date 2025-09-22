package com.example.service;

import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface ShowtimeService extends BaseService<Showtime, Long, ShowtimeReqDTO, ShowtimeResDTO> {
    ShowtimeResDTO create(ShowtimeReqDTO dto) throws IdInvalidException;
    List<ShowtimeResDTO> fetchAllByCinema(Long id) throws IdInvalidException;
}
