package com.example.service.impl;

import com.example.client.ShowtimeClient;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl
        extends BaseServiceImpl<Movie, Long, MovieReqDTO, MovieResDTO>
        implements MovieService {

    private static final String YOUTUBE_REGEX =
            "^(https?://)?(www\\.)?(youtube\\.com/watch\\?v=|youtu\\.be/)[\\w-]{11}.*$";

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final MovieMapper movieMapper;
    private final ShowtimeClient showtimeClient;

    protected MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper,
                               CategoryRepository categoryRepository, ShowtimeClient showtimeClient) {
        super(movieRepository);
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.movieMapper = movieMapper;
        this.showtimeClient = showtimeClient;
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
    public ResultPaginationDTO fetchAllMovies(String title, Long categoryId, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }

        Page<Movie> pageMovie = this.movieRepository.findAll(
                MovieSpecification.findMovieWithFilters(title, categoryId, fromDate, toDate), pageable);
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

        if (dto.getDirector() == null || dto.getDirector().trim().isEmpty()) {
            throw new IdInvalidException("Director không được để trống");
        }

        if (dto.getCast() != null && dto.getCast().trim().isEmpty()) {
            throw new IdInvalidException("Cast không được chỉ chứa khoảng trắng");
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

        if (dto.getTrailerUrl() != null && !dto.getTrailerUrl().trim().isEmpty()) {
            if (!dto.getTrailerUrl().matches(YOUTUBE_REGEX)) {
                throw new IdInvalidException("Trailer URL phải là link YouTube hợp lệ");
            }
        }

        // Map DTO -> Entity
        Movie movie = movieMapper.toEntity(dto);
        movie.setCategories(categories);
        movie.setActive(false); // mặc định khi tạo là active

        // Save
        Movie saved = movieRepository.save(movie);

        // Map Entity -> ResDTO
        return movieMapper.toDto(saved);
    }

    @Override
    public MovieResDTO changeStatus(Long id) throws IdInvalidException {
        Movie movie = movieRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Phim không tồn tại trong hệ thống!")
        );

        boolean currentStatus = movie.isActive();
        boolean newStatus = !currentStatus;

        if (newStatus) { // chỉ check khi muốn enable
            if (movie.getEndDate() != null && movie.getEndDate().isBefore(LocalDate.now())) {
                throw new IdInvalidException("Không thể bật phim: đã hết hạn (endDate).");
            }
        } else {
            showtimeClient.disableShowtimesByMovie(id);
        }

        movie.setActive(newStatus);
        return movieMapper.toDto(movieRepository.save(movie));
    }

    @Override
    public MovieResDTO updateMovie(Long id, MovieReqDTO dto) throws IdInvalidException {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Movie không tồn tại!"));

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new IdInvalidException("Title không được để trống");
        }

        if (dto.getDirector() == null || dto.getDirector().trim().isEmpty()) {
            throw new IdInvalidException("Director không được để trống");
        }

        if (dto.getCast() != null && dto.getCast().trim().isEmpty()) {
            throw new IdInvalidException("Cast không được chỉ chứa khoảng trắng");
        }

        // Unique constraint: title + releaseDate
        if (movieRepository.existsByTitleAndReleaseDate(dto.getTitle(), dto.getReleaseDate())
                && (!dto.getTitle().equals(movie.getTitle()) || !dto.getReleaseDate().equals(movie.getReleaseDate()))) {
            throw new IdInvalidException("Phim với tiêu đề và ngày phát hành này đã tồn tại!");
        }

        // Validate duration
        if (dto.getDurationInMinutes() <= 0 || dto.getDurationInMinutes() > 600) {
            throw new IdInvalidException("Thời lượng phim không hợp lệ!");
        }

        // Validate release/end date
        if (dto.getEndDate() != null && dto.getReleaseDate().isAfter(dto.getEndDate())) {
            throw new IdInvalidException("Release Date không được sau End Date!");
        }

        // Nếu movie đang active
        if (movie.isActive()) {
            LocalDate today = LocalDate.now();
            if (dto.getReleaseDate().isAfter(today)) {
                throw new IdInvalidException("Không thể đặt Release Date trong tương lai cho phim đang active!");
            }
            if (dto.getEndDate() != null && dto.getEndDate().isBefore(today)) {
                throw new IdInvalidException("End Date không được trước hôm nay cho phim đang active!");
            }
        }

        if (dto.getTrailerUrl() != null && !dto.getTrailerUrl().trim().isEmpty()) {
            if (!dto.getTrailerUrl().matches(YOUTUBE_REGEX)) {
                throw new IdInvalidException("Trailer URL phải là link YouTube hợp lệ");
            }
        }

        // Update các field
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDurationInMinutes(dto.getDurationInMinutes());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setEndDate(dto.getEndDate());
        movie.setDirector(dto.getDirector());
        movie.setCast(dto.getCast());
        movie.setPosterKey(dto.getPosterKey());
        movie.setTrailerUrl(dto.getTrailerUrl());

        // Update categories
        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        movie.setCategories(categories);

        Movie updated = movieRepository.save(movie);
        return movieMapper.toDto(updated);
    }

    @Override
    public List<MovieResDTO> fetchShowingMovies(int limit) {
        return movieRepository.findNowShowingMovies
                (LocalDate.now(), PageRequest.of(0, limit)).stream().map(
                movieMapper::toDto).toList();
    }

    @Override
    public List<MovieResDTO> getComingSoonMovies(int limit) {
        return movieRepository.findComingSoonMovies
                (LocalDate.now(), PageRequest.of(0, limit)).stream().map(
                movieMapper::toDto).toList();
    }
}
