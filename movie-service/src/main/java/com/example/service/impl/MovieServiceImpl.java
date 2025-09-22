package com.example.service.impl;

import com.example.domain.entity.Movie;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.mapper.MovieMapper;
import com.example.repository.BaseRepository;
import com.example.repository.MovieRepository;
import com.example.service.MovieService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl
        extends BaseServiceImpl<Movie, Long, MovieReqDTO, MovieResDTO>
        implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    protected MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
        super(movieRepository);
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public MovieResDTO getById(Long id) throws IdInvalidException {
        return movieMapper.toDto(movieRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Movie không tồn tại trong hệ thống!")
        ));
    }

    @Override
    public List<MovieResDTO> getByIds(List<Long> ids) {
        List<Movie> movies = movieRepository.findAllById(ids);
        return movies.stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieResDTO> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public List<MovieResDTO> getAllActive() {
        List<Movie> movies = movieRepository.findByActiveTrue();
        return movies.stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }


}
