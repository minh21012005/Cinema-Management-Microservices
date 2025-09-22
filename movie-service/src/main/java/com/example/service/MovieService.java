package com.example.service;

import com.example.domain.entity.Movie;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface MovieService extends BaseService<Movie, Long, MovieReqDTO, MovieResDTO> {
    MovieResDTO getById(Long id) throws IdInvalidException;
    List<MovieResDTO> getByIds(List<Long> ids) throws IdInvalidException;
    List<MovieResDTO> searchByTitle(String title);
}
