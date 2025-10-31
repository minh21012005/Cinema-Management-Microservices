package com.example.service.impl;

import com.example.client.MovieClient;
import com.example.client.UserClient;
import com.example.domain.entity.*;
import com.example.domain.request.ItemDTO;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.mapper.ShowtimeMapper;
import com.example.repository.*;
import com.example.service.ShowtimeService;
import com.example.service.specification.ShowtimeSpecification;
import com.example.util.error.IdInvalidException;
import feign.FeignException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final UserClient userClient;
    private final SeatRepository seatRepository;
    private final FoodRepository foodRepository;
    private final ComboRepository comboRepository;

    protected ShowtimeServiceImpl(ShowtimeRepository showtimeRepository,
                                  RoomRepository roomRepository,
                                  ShowtimeMapper showtimeMapper,
                                  CinemaRepository cinemaRepository,
                                  MovieClient movieClient,
                                  UserClient userClient,
                                  SeatRepository seatRepository,
                                  FoodRepository foodRepository,
                                  ComboRepository comboRepository) {
        super(showtimeRepository);
        this.roomRepository = roomRepository;
        this.showtimeRepository = showtimeRepository;
        this.movieClient = movieClient;
        this.showtimeMapper = showtimeMapper;
        this.cinemaRepository = cinemaRepository;
        this.userClient = userClient;
        this.seatRepository = seatRepository;
        this.foodRepository = foodRepository;
        this.comboRepository = comboRepository;
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
    public ResultPaginationDTO fetchAllByCinema(Long cinemaId, String title, Long roomId, LocalDate fromDate,
                                                LocalDate toDate, Pageable pageable) throws IdInvalidException {
        List<Long> movieIds = null;
        if (title != null && !title.isBlank()) {
            // gọi movie-service để tìm movieIds theo title
            movieIds = movieClient.findByTitle(title)
                    .getData()
                    .stream()
                    .map(MovieResDTO::getId)
                    .toList();
        }

        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "startTime")
            );
        }

        Specification<Showtime> spec = ShowtimeSpecification.findShowtimesWithFilters(
                cinemaId, roomId, movieIds, fromDate, toDate);
        Page<Showtime> pageShowtimes = showtimeRepository.findAll(spec, pageable);

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
            validateShowtime(showtime.getRoom().getId(), id, showtime.getStartTime(), endTime);
        }

        showtime.setActive(!showtime.isActive());
        return showtimeMapper.toDto(showtimeRepository.save(showtime));
    }

    @Override
    public ShowtimeResDTO updateShowtime(Long id, ShowtimeReqDTO dto) throws IdInvalidException {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Showtime không tồn tại!"));

        if (showtime.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Không thể update suất chiếu đã bắt đầu!");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IdInvalidException("Room không tồn tại!"));

        if (!room.isActive() || !room.getCinema().isActive()) {
            throw new IllegalArgumentException("Room hoặc Cinema không hoạt động!");
        }

        MovieResDTO movie = movieClient.findById(dto.getMovieId()).getData();
        if (movie == null || !movie.isActive()) {
            throw new IllegalArgumentException("Movie không hợp lệ hoặc không active!");
        }

        LocalDateTime newStartTime = dto.getStartTime();
        LocalDateTime newEndTime = newStartTime.plusMinutes(movie.getDurationInMinutes());

        if ((movie.getReleaseDate() != null && newStartTime.toLocalDate().isBefore(movie.getReleaseDate())) ||
                (movie.getEndDate() != null && newStartTime.toLocalDate().isAfter(movie.getEndDate()))) {
            throw new IllegalArgumentException("Thời gian chiếu không nằm trong releaseDate và endDate của movie!");
        }

        // Check trùng giờ chiếu trong phòng
        validateShowtime(room.getId(), id, newStartTime, newEndTime);

        // Update
        showtime.setStartTime(newStartTime);
        showtime.setEndTime(newEndTime);
        showtime.setRoom(room);
        showtime.setMovieId(movie.getId());

        Showtime saved = showtimeRepository.save(showtime);
        ShowtimeResDTO resDTO = showtimeMapper.toDto(saved);
        resDTO.setMovieTitle(movie.getTitle());
        return resDTO;
    }

    @Override
    public void disableShowtimesByMovie(Long movieId) {
        List<Showtime> futureShowtimes = showtimeRepository
                .findByMovieIdAndActiveTrueAndStartTimeAfter(movieId, LocalDateTime.now());

        if (!futureShowtimes.isEmpty()) {
            // Disable tất cả
            futureShowtimes.forEach(showtime -> showtime.setActive(false));

            // Lưu tất cả cùng lúc
            showtimeRepository.saveAll(futureShowtimes);
        }

        // Trả về DTO
        futureShowtimes.stream()
                .map(showtimeMapper::toDto)
                .toList();
    }

    @Override
    public ResultPaginationDTO fetchShowtimeInDayForStaff(String title, Pageable pageable) throws IdInvalidException {
        Long cinemaId = userClient.findCinemaIdByUser().getData();

        // check cinema tồn tại
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new IdInvalidException("Cinema không tồn tại trong hệ thống!"));

        if (cinema.getRooms().isEmpty()) {
            throw new IdInvalidException("Cinema hiện chưa có phòng chiếu!");
        }

        List<Long> movieIds = null;
        if (title != null && !title.isBlank()) {
            // gọi movie-service để tìm movieIds theo title
            movieIds = movieClient.findByTitle(title)
                    .getData()
                    .stream()
                    .map(MovieResDTO::getId)
                    .toList();
        }

        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.ASC, "startTime")
            );
        }

        // build spec
        Specification<Showtime> spec = ShowtimeSpecification.findShowtimesWithFilterForStaff(cinemaId, movieIds);

        // query có phân trang
        Page<Showtime> pageShowtimes = showtimeRepository.findAll(spec, pageable);

        // Lấy tất cả movieId duy nhất trong page
        List<Long> idsInPage = pageShowtimes.getContent().stream()
                .map(Showtime::getMovieId)
                .distinct()
                .toList();

        // Gọi 1 lần API để lấy danh sách phim
        List<MovieResDTO> movies = movieClient.findByIds(idsInPage).getData();

        // Tạo map để lookup nhanh
        Map<Long, MovieResDTO> movieMap = movies.stream()
                .collect(Collectors.toMap(MovieResDTO::getId, m -> m));

        // Map sang ShowtimeResDTO
        List<ShowtimeResDTO> result = pageShowtimes.getContent().stream()
                .map(showtime -> {
                    ShowtimeResDTO resDTO = showtimeMapper.toDto(showtime);

                    MovieResDTO movie = movieMap.get(showtime.getMovieId());
                    if (movie != null) {
                        resDTO.setMovieTitle(movie.getTitle());
                        resDTO.setPosterKey(movie.getPosterKey());
                    }

                    return resDTO;
                })
                .toList();

        // build response
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageShowtimes.getNumber());
        mt.setPageSize(pageShowtimes.getSize());
        mt.setPages(pageShowtimes.getTotalPages());
        mt.setTotal(pageShowtimes.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(result);

        return rs;
    }

    @Override
    public boolean isShowtimeEnd(Long id) throws IdInvalidException {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Showtime không tồn tại!"));
        return LocalDateTime.now().isAfter(showtime.getEndTime());
    }

    @Override
    public List<ShowtimeResDTO> fetchShowtimesActiveByMovie
            (Long id, String date, Long cinemaId) throws IdInvalidException {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        if (date != null && !date.isEmpty()) {
            LocalDate localDate = LocalDate.parse(date); // yyyy-MM-dd
            startDateTime = localDate.atStartOfDay();
            endDateTime = localDate.plusDays(1).atStartOfDay();
        } else {
            startDateTime = LocalDateTime.now();
            endDateTime = LocalDateTime.MAX;
        }

        return showtimeRepository.findActiveShowtimesByMovieAndOptionalCinema
                        (id, cinemaId, startDateTime, endDateTime)
                .stream()
                .map(showtimeMapper::toDto)
                .toList();
    }

    @Override
    public TicketEmailDTO fetchTicketData(Long id, List<Long> seatIds, List<ItemDTO> foods, List<ItemDTO> combos)
            throws IdInvalidException {

        Showtime showtime = showtimeRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Show time không hợp lệ")
        );

        MovieResDTO movieResDTO = movieClient.findById(showtime.getMovieId()).getData();

        // ✅ Lấy danh sách ghế
        List<String> seatCodes = seatRepository.findAllById(seatIds)
                .stream()
                .map(Seat::getName)
                .toList();

        // ✅ Gán lại tên cho từng food (giữ nguyên quantity)
        if (foods != null && !foods.isEmpty()) {
            List<Long> foodIds = foods.stream().map(ItemDTO::getId).toList();
            Map<Long, String> foodNameMap = foodRepository.findAllById(foodIds).stream()
                    .collect(Collectors.toMap(Food::getId, Food::getName));

            foods.forEach(f -> f.setName(foodNameMap.getOrDefault(f.getId(), "Không rõ")));
        }

        // ✅ Gán lại tên cho từng combo (giữ nguyên quantity)
        if (combos != null && !combos.isEmpty()) {
            List<Long> comboIds = combos.stream().map(ItemDTO::getId).toList();
            Map<Long, String> comboNameMap = comboRepository.findAllById(comboIds).stream()
                    .collect(Collectors.toMap(Combo::getId, Combo::getName));

            combos.forEach(c -> c.setName(comboNameMap.getOrDefault(c.getId(), "Không rõ")));
        }

        Room room = showtime.getRoom();
        Cinema cinema = room.getCinema();

        TicketEmailDTO dto = new TicketEmailDTO();
        dto.setCinemaName(cinema.getName());
        dto.setRoomName(room.getName());
        dto.setShowtime(showtime.getStartTime());
        dto.setMovieTitle(movieResDTO.getTitle());
        dto.setSeatCodes(seatCodes);
        dto.setFoods(foods);
        dto.setCombos(combos);

        return dto;
    }

    @Override
    public Long getActiveShowtimesCount() {
        return showtimeRepository.countActiveShowtimes(LocalDateTime.now());
    }

    @Override
    public Long getNowShowingMoviesCount() {
        return showtimeRepository.countMoviesNowShowing(LocalDateTime.now());
    }

    @Override
    public Map<String, Double> getTopMovieRevenue(Map<Long, Double> showtimeToRevenue) {
        // 1️⃣ Lấy tất cả showtime liên quan trong 1 lần
        List<Showtime> showtimes = showtimeRepository.findAllById(showtimeToRevenue.keySet());
        Map<Long, Long> showtimeIdToMovieId = showtimes.stream()
                .collect(Collectors.toMap(Showtime::getId, Showtime::getMovieId));

        // 2️⃣ Nhóm doanh thu theo movieId
        Map<Long, Double> movieToRevenue = new HashMap<>();
        showtimeToRevenue.forEach((showtimeId, revenue) -> {
            Long movieId = showtimeIdToMovieId.get(showtimeId);
            if (movieId != null) {
                movieToRevenue.merge(movieId, revenue, Double::sum);
            }
        });

        // 3️⃣ Lấy thông tin movie
        List<Long> movieIds = new ArrayList<>(movieToRevenue.keySet());
        List<MovieResDTO> movies = movieClient.findByIds(movieIds).getData();

        Map<String, Double> result = new HashMap<>();
        movieToRevenue.forEach((movieId, revenue) -> {
            String title = movies.stream()
                    .filter(m -> m.getId().equals(movieId))
                    .map(MovieResDTO::getTitle)
                    .findFirst()
                    .orElse("Unknown Movie");
            result.put(title, revenue);
        });

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ShowtimeResDTO> importExcel(MultipartFile file) throws IdInvalidException {
        List<ShowtimeResDTO> createdShowtimes = new ArrayList<>();

        try (InputStream is = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // bỏ header
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Long roomId = (long) row.getCell(0).getNumericCellValue();
                Long movieId = (long) row.getCell(1).getNumericCellValue();

                Cell timeCell = row.getCell(2);
                String startTimeStr;

                if (timeCell == null) continue;

                // ⚙️ Nếu Excel lưu dạng text
                if (timeCell.getCellType() == CellType.STRING) {
                    startTimeStr = timeCell.getStringCellValue();
                }
                // ⚙️ Nếu Excel lưu dạng ngày (DATE)
                else if (timeCell.getCellType() == CellType.NUMERIC) {
                    startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm")
                            .format(timeCell.getDateCellValue());
                } else {
                    continue;
                }

                LocalDateTime startTime = LocalDateTime.parse(
                        startTimeStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                );

                ShowtimeReqDTO dto = new ShowtimeReqDTO();
                dto.setRoomId(roomId);
                dto.setMovieId(movieId);
                dto.setStartTime(startTime);

                ShowtimeResDTO created = this.create(dto);
                createdShowtimes.add(created);
            }

            workbook.close();
        } catch (IdInvalidException e) {
            throw new IdInvalidException("Lỗi khi tạo suất chiếu: " + e.getMessage());
        } catch (IOException e) {
            throw new IdInvalidException("Không thể đọc file Excel: " + e.getMessage());
        }
        return createdShowtimes;
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
