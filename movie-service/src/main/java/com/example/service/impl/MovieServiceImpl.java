package com.example.service.impl;

import com.example.domain.entity.Category;
import com.example.domain.entity.Movie;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.MovieMapper;
import com.example.repository.CategoryRepository;
import com.example.repository.MovieRepository;
import com.example.service.MovieService;
import com.example.service.specification.MovieSpecification;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl
        extends BaseServiceImpl<Movie, Long, MovieReqDTO, MovieResDTO>
        implements MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final MovieMapper movieMapper;

    protected MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper,
                               CategoryRepository categoryRepository) {
        super(movieRepository);
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
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

    @Override
    public ResultPaginationDTO fetchAllMovies(String title, Long categoryId, Pageable pageable) {
        Page<Movie> pageMovie = this.movieRepository.findAll(
                MovieSpecification.findMovieWithFilters(title, categoryId), pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageMovie.getTotalPages());
        mt.setTotal(pageMovie.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<MovieResDTO> listCinema = pageMovie.getContent()
                .stream().map(movieMapper::toDto)
                .collect(Collectors.toList());

        rs.setResult(listCinema);

        return rs;
    }

    @Override
    public MovieResDTO createMovie(MovieReqDTO dto) throws IdInvalidException {
        // Validate title
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IdInvalidException("Title không được để trống");
        }

        // Validate description
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            throw new IdInvalidException("Description không được để trống");
        }

        // Validate duration
        if (dto.getDurationInMinutes() <= 0) {
            throw new IdInvalidException("Duration phải lớn hơn 0");
        }

        // Validate releaseDate
        if (dto.getReleaseDate() == null) {
            throw new IdInvalidException("Release date không được để trống");
        }

        // Nếu có endDate thì phải sau releaseDate
        if (dto.getEndDate() != null && dto.getEndDate().isBefore(dto.getReleaseDate())) {
            throw new IdInvalidException("End date phải sau hoặc bằng release date");
        }

        // Validate poster
        if (dto.getPosterKey() == null || dto.getPosterKey().trim().isEmpty()) {
            throw new IdInvalidException("Poster không được để trống");
        }

        // Validate category
        List<Category> categories = new ArrayList<>();
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            categories = categoryRepository.findAllById(dto.getCategoryIds());

            if (categories.size() != dto.getCategoryIds().size()) {
                throw new IdInvalidException("Một số categoryId không tồn tại");
            }
        }

        if (categories.isEmpty()) {
            throw new IdInvalidException("Phim không có thể loại.");
        }

        // ✅ Check trùng title + releaseDate
        boolean exists = movieRepository.existsByTitleAndReleaseDate(dto.getTitle().trim(), dto.getReleaseDate());
        if (exists) {
            throw new IdInvalidException("Phim với cùng tên và ngày phát hành đã tồn tại.");
        }

        // Map DTO -> Entity
        Movie movie = movieMapper.toEntity(dto);
        movie.setCategories(categories);
        movie.setActive(true); // mặc định khi tạo là active

        // Save
        Movie saved = movieRepository.save(movie);

        // Map Entity -> ResDTO
        return movieMapper.toDto(saved);
    }

}
