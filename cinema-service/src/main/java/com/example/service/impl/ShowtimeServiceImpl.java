package com.example.service.impl;

import com.example.client.MovieClient;
import com.example.domain.entity.Cinema;
import com.example.domain.entity.Room;
import com.example.domain.entity.Showtime;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.mapper.ShowtimeMapper;
import com.example.repository.CinemaRepository;
import com.example.repository.RoomRepository;
import com.example.repository.ShowtimeRepository;
import com.example.service.ShowtimeService;
import com.example.util.error.IdInvalidException;
import feign.FeignException;
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

        MovieResDTO movie;
        try {
            movie = movieClient.findById(dto.getMovieId()).getData();
        } catch (FeignException.BadRequest e) {
            throw new IdInvalidException("Movie ID không hợp lệ: " + dto.getMovieId());
        }

        LocalDateTime start = dto.getStartTime();
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
    public List<ShowtimeResDTO> fetchAllByCinema(Long id) throws IdInvalidException {
        Cinema cinema = cinemaRepository.findById(id).
                orElseThrow(() -> new IdInvalidException("Cinema không tồn tại trong hệ thống"));

        // Lấy toàn bộ showtimes của tất cả room trong cinema
        List<Showtime> showtimes = cinema.getRooms().stream()
                .flatMap(room -> room.getShowtimes().stream())
                .toList();

        if (showtimes.isEmpty()) {
            throw new IdInvalidException("Cinema chưa có suất chiếu nào!");
        }

        // Lấy tất cả movieId duy nhất
        List<Long> movieIds = showtimes.stream()
                .map(Showtime::getMovieId)
                .distinct()
                .toList();

        // Gọi 1 lần API để lấy danh sách phim
        List<MovieResDTO> movies = movieClient.findByIds(movieIds).getData();

        // Tạo map cho nhanh lookup
        Map<Long, String> movieMap = movies.stream()
                .collect(Collectors.toMap(MovieResDTO::getId, MovieResDTO::getTitle));

        // Map sang ShowtimeResDTO
        return showtimes.stream()
                .map(showtime -> {
                    ShowtimeResDTO resDTO = showtimeMapper.toDto(showtime);
                    resDTO.setMovieTitle(movieMap.get(showtime.getMovieId())); // lấy title từ map
                    return resDTO;
                })
                .collect(Collectors.toList());
    }


    public void validateShowtime(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Showtime> overlaps = showtimeRepository.findOverlappingShowtimes(roomId, startTime, endTime);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Giờ chiếu bị trùng với suất chiếu khác trong cùng phòng!");
        }
    }
}
