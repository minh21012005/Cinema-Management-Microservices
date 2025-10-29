package com.example.controller;

import com.example.domain.entity.Showtime;
import com.example.domain.entity.TicketEmailDTO;
import com.example.domain.request.ShowtimeReqDTO;
import com.example.domain.request.TicketDataRequest;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.domain.response.ShowtimeResDTO;
import com.example.mapper.ShowtimeMapper;
import com.example.service.ShowtimeService;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/showtime")
public class ShowtimeController extends BaseController<Showtime, Long, ShowtimeReqDTO, ShowtimeResDTO> {

    private final ShowtimeService showtimeService;

    protected ShowtimeController(ShowtimeService showtimeService, ShowtimeMapper mapper) {
        super(showtimeService, mapper);
        this.showtimeService = showtimeService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SHOWTIME_CREATE')")
    public ResponseEntity<ShowtimeResDTO> create(@RequestBody ShowtimeReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(showtimeService.create(dto));
    }

    @GetMapping("/cinemas/{id}")
    @PreAuthorize("hasPermission(null, 'SHOWTIME_VIEW')")
    public ResponseEntity<ResultPaginationDTO> fetchShowtimeByCinema(
            @PathVariable("id") Long cinemaId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "roomId", required = false) Long roomId,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.fetchAllByCinema(cinemaId, title, roomId, fromDate, toDate, pageable));
    }

    @GetMapping("/inday")
    @PreAuthorize("hasPermission(null, 'SHOWTIME_VIEW')")
    public ResponseEntity<ResultPaginationDTO> fetchShowtimeInDayForStaff(
            @RequestParam(name = "title", required = false) String title,
            Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.fetchShowtimeInDayForStaff(title, pageable));
    }

    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasPermission(null, 'SHOWTIME_UPDATE')")
    public ResponseEntity<ShowtimeResDTO> changeShowtimeStatus(
            @PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.changeStatus(id));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SHOWTIME_UPDATE')")
    public ResponseEntity<ShowtimeResDTO> update(@PathVariable("id") Long id, @RequestBody ShowtimeReqDTO dto)
            throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.updateShowtime(id, dto));
    }

    @PutMapping("/disable-by-movie/{id}")
    @PreAuthorize("hasPermission(null, 'SHOWTIME_UPDATE')")
    public ResponseEntity<Void> disableShowtimesByMovie(@PathVariable("id") Long id) {
        showtimeService.disableShowtimesByMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<ShowtimeResDTO>> fetchShowtimesActiveByMovie(
            @PathVariable("id") Long id,
            @RequestParam(name = "date", required = false) String date, // yyyy-MM-dd
            @RequestParam(name = "cinemaId", required = false) Long cinemaId
    ) throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.fetchShowtimesActiveByMovie(id, date, cinemaId));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SHOWTIME_VIEW')")
    public ResponseEntity<Showtime> getById(@PathVariable("id") Long id) throws IdInvalidException {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SHOWTIME_VIEW')")
    public ResponseEntity<List<Showtime>> getAll() {
        return super.getAll();
    }

    @GetMapping("/{id}/is-end")
    @PreAuthorize("hasPermission(null, 'SHOWTIME_VIEW')")
    public boolean isShowtimeEnd(@PathVariable("id") Long id) throws IdInvalidException {
        return showtimeService.isShowtimeEnd(id);
    }

    @PostMapping("/{id}/ticket-data")
    public ResponseEntity<TicketEmailDTO> fetchTicketData(
            @PathVariable("id") Long id,
            @RequestBody TicketDataRequest request
    ) throws IdInvalidException {
        return ResponseEntity.ok(showtimeService.fetchTicketData(
                id,
                request.getSeatIds(),
                request.getFoods(),
                request.getCombos()));
    }

    @GetMapping("/active")
    public ResponseEntity<Long> getActiveShowtimes() {
        return ResponseEntity.ok(showtimeService.getActiveShowtimesCount());
    }

    @GetMapping("/now-showing")
    public ResponseEntity<Long> getNowShowingMovies() {
        return ResponseEntity.ok(showtimeService.getNowShowingMoviesCount());
    }

    @PostMapping("/movie-revenue")
    public ResponseEntity<Map<String, Double>> getTopMovieRevenue
            (@RequestBody Map<Long, Double> showtimeToRevenue) {
        return ResponseEntity.ok(showtimeService.getTopMovieRevenue(showtimeToRevenue));
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws IdInvalidException {
        throw new UnsupportedOperationException("Delete showtime is not supported!");
    }
}
