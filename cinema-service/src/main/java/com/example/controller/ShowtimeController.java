package com.example.controller;

import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.mapper.ShowtimeMapper;
import com.example.service.ShowtimeService;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/showtime")
public class ShowtimeController extends BaseController<Showtime, Long, ShowtimeReqDTO, ShowtimeResDTO> {

    private final ShowtimeService showtimeService;

    protected ShowtimeController(ShowtimeService showtimeService, ShowtimeMapper mapper) {
        super(showtimeService, mapper);
        this.showtimeService = showtimeService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SHOWTIME_CREATE')")
    public ResponseEntity<ShowtimeResDTO> create(ShowtimeReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(showtimeService.create(dto));
    }

    @GetMapping("/cinemas/{id}")
    @PreAuthorize("hasPermission(null, 'SHOWTIME_VIEW')")
    public ResponseEntity<ResultPaginationDTO> fetchShowtimeByCinema(
            @PathVariable("id") Long cinemaId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "roomId", required = false) Long roomId,
            Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.fetchAllByCinema(cinemaId, title, roomId, pageable));
    }

}
