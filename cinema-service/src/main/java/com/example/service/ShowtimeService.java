package com.example.service;

import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

public interface ShowtimeService extends BaseService<Showtime, Long, ShowtimeReqDTO, ShowtimeResDTO> {
    ShowtimeResDTO create(ShowtimeReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO fetchAllByCinema(Long cinemaId, String title, Long roomId, Pageable pageable)
            throws IdInvalidException;
}
