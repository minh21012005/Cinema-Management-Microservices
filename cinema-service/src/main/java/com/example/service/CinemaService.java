package com.example.service;

import com.example.domain.entity.Cinema;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import com.example.util.error.IdInvalidException;

public interface CinemaService extends BaseService<Cinema, Long, CinemaReqDTO, CinemaResDTO> {
    CinemaResDTO createCinema(CinemaReqDTO dto) throws IdInvalidException;
}
