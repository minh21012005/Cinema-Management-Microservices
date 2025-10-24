package com.example.service;

import com.example.domain.entity.Movie;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface MovieService extends BaseService<Movie, Long, MovieReqDTO, MovieResDTO> {
    MovieResDTO getById(Long id) throws IdInvalidException;
    List<MovieResDTO> getByIds(List<Long> ids) throws IdInvalidException;
    List<MovieResDTO> searchByTitle(String title);
    List<MovieResDTO> getAllActive();
    ResultPaginationDTO fetchAllMovies(
            String title, Long categoryId, LocalDate fromDate, LocalDate toDate,Pageable pageable);
    MovieResDTO createMovie(MovieReqDTO dto) throws IdInvalidException;
    MovieResDTO changeStatus(Long id) throws IdInvalidException;
    MovieResDTO updateMovie(Long id, MovieReqDTO dto) throws IdInvalidException;
    List<MovieResDTO> fetchShowingMovies(int limit);
    List<MovieResDTO> getComingSoonMovies(int limit);
    List<MovieResDTO> getSimilarMovies(Long movieId, int topN) throws IdInvalidException;
}
