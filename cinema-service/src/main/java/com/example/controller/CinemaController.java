package com.example.controller;

import com.example.domain.entity.Cinema;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import com.example.mapper.CinemaMapper;
import com.example.service.CinemaService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cinemas")
public class CinemaController extends BaseController<Cinema, Long, CinemaReqDTO, CinemaResDTO>{

    private final CinemaService cinemaService;
    private final CinemaMapper cinemaMapper;

    protected CinemaController(CinemaService cinemaService, CinemaMapper cinemaMapper) {
        super(cinemaService, cinemaMapper);
        this.cinemaService = cinemaService;
        this.cinemaMapper = cinemaMapper;
    }

    @Override
    public ResponseEntity<CinemaResDTO> create(CinemaReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(cinemaService.createCinema(dto));
    }
}
