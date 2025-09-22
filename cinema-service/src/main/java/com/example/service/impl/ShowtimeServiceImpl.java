package com.example.service.impl;

import com.example.client.MovieClient;
import com.example.domain.entity.Room;
import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.mapper.ShowtimeMapper;
import com.example.repository.CinemaRepository;
import com.example.repository.RoomRepository;
import com.example.repository.ShowtimeRepository;
import com.example.service.ShowtimeService;
import com.example.service.specification.ShowtimeSpecification;
import com.example.util.error.IdInvalidException;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShowtimeServiceImpl
        extends BaseServiceImpl<Showtime, Long, ShowtimeReqDTO, ShowtimeResDTO>
        implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final CinemaRepository cinemaRepository;
    private final RoomRepository roomRepository;
    private final MovieClient movieClient;
    private final ShowtimeMapper showtimeMapper;

    protected ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,
                                  RoomRepository roomRepository,
                                  ShowtimeMapper showtimeMapper,
                                  CinemaRepository cinemaRepository,
                                  MovieClient movieClient) {
        super(showtimeRepository);
        this.roomRepository = roomRepository;
        this.showtimeRepository = showtimeRepository;
        this.movieClient = movieClient;
        this.showtimeMapper = showtimeMapper;
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public ShowtimeResDTO create(ShowtimeReqDTO dto) throws IdInvalidException {
        Room room = roomRepository.findById(dto.getRoomId()).orElseThrow(
                () -> new IdInvalidException("Room không tồn tại trong hệ thống!")
        );

        if (!room.getCinema().isActive()) {
            throw new IllegalArgumentException("Cinema của phòng chiếu không hoạt động!");
        }

        if (!room.isActive()) {
            throw new IllegalArgumentException("Room đang không hoạt động!");
        }

        MovieResDTO movie;
        try {
            movie = movieClient.findById(dto.getMovieId()).getData();
        } catch (FeignException.BadRequest e) {
            throw new IdInvalidException("Movie ID không hợp lệ: " + dto.getMovieId());
        }

        if (movie == null) throw new IdInvalidException("Movie không tồn tại: " + dto.getMovieId());
        if (!movie.isActive()) {
            throw new IllegalArgumentException("Movie đang không active, không thể tạo suất chiếu cho phim này!");
        }
        if (movie.getDurationInMinutes() <= 0) {
            throw new IllegalArgumentException("Duration của movie không hợp lệ!");
        }

        LocalDateTime start = dto.getStartTime();

        if (movie.getReleaseDate() != null && start.toLocalDate().isBefore(movie.getReleaseDate())) {
            throw new IllegalArgumentException("Không thể tạo suất trước ngày phát hành phim!");
        }
        if (movie.getEndDate() != null && start.toLocalDate().isAfter(movie.getEndDate())) {
            throw new IllegalArgumentException("Không thể tạo suất sau ngày kết thúc chiếu của phim!");
        }

        // ✅ Validate startTime không được ở quá khứ
        if (start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Thời gian bắt đầu không được ở quá khứ!");
        }

        LocalDateTime end = start.plusMinutes(movie.getDurationInMinutes());

        validateShowtime(dto.getRoomId(), dto.getStartTime(), end);

        Showtime showtime = new Showtime();
        showtime.setRoom(room);
        showtime.setStartTime(dto.getStartTime());
        showtime.setEndTime(end);
        showtime.setActive(true);
        showtime.setMovieId(movie.getId());

        Showtime saved = showtimeRepository.save(showtime);
        ShowtimeResDTO resDTO = showtimeMapper.toDto(saved);
        resDTO.setMovieTitle(movie.getTitle());

        return resDTO;
    }

    @Override
    public ResultPaginationDTO fetchAllByCinema(Long cinemaId, String title, Long roomId, Pageable pageable)
            throws IdInvalidException {
        List<Long> movieIds = null;
        if (title != null && !title.isBlank()) {
            // gọi movie-service để tìm movieIds theo title
            movieIds = movieClient.findByTitle(title)
                    .getData()
                    .stream()
                    .map(MovieResDTO::getId)
                    .toList();

            // nếu không có phim nào match -> trả luôn empty page
            if (movieIds.isEmpty()) {
                throw new IdInvalidException("Không có suất chiếu tương ứng!");
            }
        }

        Specification<Showtime> spec = ShowtimeSpecification.findShowtimesWithFilters(cinemaId, roomId, movieIds);
        Page<Showtime> pageShowtimes = showtimeRepository.findAll(spec, pageable);

        if (pageShowtimes.isEmpty()) {
            throw new IdInvalidException("Không có suất chiếu tương ứng!");
        }

        // Lấy tất cả movieId duy nhất trong page
        List<Long> idsInPage = pageShowtimes.getContent().stream()
                .map(Showtime::getMovieId)
                .distinct()
                .toList();

        // Gọi 1 lần API để lấy danh sách phim
        List<MovieResDTO> movies = movieClient.findByIds(idsInPage).getData();

        // Tạo map cho nhanh lookup
        Map<Long, String> movieMap = movies.stream()
                .collect(Collectors.toMap(MovieResDTO::getId, MovieResDTO::getTitle));

        // Map sang ShowtimeResDTO
        List<ShowtimeResDTO> result = pageShowtimes.getContent().stream()
                .map(showtime -> {
                    ShowtimeResDTO resDTO = showtimeMapper.toDto(showtime);
                    resDTO.setMovieTitle(movieMap.get(showtime.getMovieId())); // lấy title từ map
                    return resDTO;
                })
                .collect(Collectors.toList());

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageShowtimes.getTotalPages());
        meta.setTotal(pageShowtimes.getTotalElements());

        // Trả về kết quả
        ResultPaginationDTO rs = new ResultPaginationDTO();
        rs.setMeta(meta);
        rs.setResult(result);

        return rs;
    }

    @Override
    public ShowtimeResDTO changeStatus(Long id) throws IdInvalidException {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Showtime không tồn tại trong hệ thống!")
        );

        if (showtime.isActive()) {
            // ACTIVE -> INACTIVE
            if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
                throw new IdInvalidException("Không thể hủy suất chiếu đã bắt đầu hoặc đã chiếu xong!");
            }
//            if (ticketRepository.existsByShowtimeId(showtime.getId())) {
//                throw new IdInvalidException("Suất chiếu đã bán vé, không thể hủy!");
//            }
        } else {
            // INACTIVE -> ACTIVE
            MovieResDTO movie;
            try {
                movie = movieClient.findById(showtime.getMovieId()).getData();
            } catch (FeignException.BadRequest e) {
                throw new IdInvalidException("Movie ID không hợp lệ: " + showtime.getMovieId());
            }

            if (!showtime.getRoom().isActive() || !showtime.getRoom().getCinema().isActive()) {
                throw new IdInvalidException("Cinema hoặc Room không hoạt động!");
            }
            if (!movie.isActive()) {
                throw new IdInvalidException("Phim không hoạt động!");
            }
            if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
                throw new IdInvalidException("Thời gian chiếu phải nằm trong tương lai!");
            }

            LocalDateTime endTime = showtime.getStartTime()
                    .plusMinutes(movie.getDurationInMinutes());
            validateShowtime(showtime.getRoom().getId(), id,showtime.getStartTime(), endTime);
        }

        showtime.setActive(!showtime.isActive());
        return showtimeMapper.toDto(showtimeRepository.save(showtime));
    }


    public void validateShowtime(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Showtime> overlaps = showtimeRepository.findOverlappingShowtimes(roomId, startTime, endTime);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Giờ chiếu bị trùng với suất chiếu khác trong cùng phòng!");
        }
    }

    public void validateShowtime(Long roomId, Long showtimeId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Showtime> overlaps = showtimeRepository.findOverlappingShowtimesExcept(roomId, showtimeId, startTime, endTime);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Giờ chiếu bị trùng với suất chiếu khác trong cùng phòng!");
        }
    }

}
