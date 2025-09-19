package com.example.service;

import com.example.domain.entity.Cinema;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

public interface CinemaService extends BaseService<Cinema, Long, CinemaReqDTO, CinemaResDTO> {
    CinemaResDTO createCinema(CinemaReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO fetchAllCinema(String name, Pageable pageable);
    boolean existsByName(String name);
    boolean existsByPhone(String phone);
    boolean existsByAddress(String address);
    Cinema changeStatusOfCinema(Long id);
}
