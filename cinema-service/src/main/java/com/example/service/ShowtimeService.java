package com.example.service;

import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ShowtimeService extends BaseService<Showtime, Long, ShowtimeReqDTO, ShowtimeResDTO> {
    ShowtimeResDTO create(ShowtimeReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO fetchAllByCinema(Long cinemaId, String title, Long roomId, LocalDate fromDate,
                                         LocalDate toDate, Pageable pageable) throws IdInvalidException;
    ShowtimeResDTO changeStatus(Long id) throws IdInvalidException;
    ShowtimeResDTO updateShowtime(Long id, ShowtimeReqDTO dto) throws IdInvalidException;
    void disableShowtimesByMovie(Long movieId);
    ResultPaginationDTO fetchShowtimeInDayForStaff(String title, Pageable pageable) throws IdInvalidException;
    boolean isShowtimeEnd(Long id) throws IdInvalidException;
    List<ShowtimeResDTO> fetchShowtimesActiveByMovie
            (Long id, String date, Long cinemaId) throws IdInvalidException;
}
