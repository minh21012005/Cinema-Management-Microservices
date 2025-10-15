package com.example.service;

import com.example.domain.entity.Rating;
import com.example.domain.request.RatingReqDTO;
import com.example.domain.response.RatingResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

public interface RatingService extends BaseService<Rating, Long, RatingReqDTO, RatingResDTO> {
    RatingResDTO create(RatingReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO getRatingsByMovie(Long id, Pageable pageable) throws IdInvalidException;
}
