package com.example.controller;

import com.example.domain.entity.Movie;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.MovieMapper;
import com.example.service.MovieService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController extends BaseController<Movie, Long, MovieReqDTO, MovieResDTO> {

    private final MovieService movieService;

    protected MovieController(MovieService movieService, MovieMapper movieMapper) {
        super(movieService, movieMapper);
        this.movieService = movieService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public ResponseEntity<ResultPaginationDTO> fetchAllMovies(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Pageable pageable) throws IdInvalidException {
        return ResponseEntity.ok(movieService.fetchAllMovies(title, categoryId, fromDate, toDate, pageable));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'MOVIE_CREATE')")
    public ResponseEntity<MovieResDTO> create(@RequestBody MovieReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(dto));
    }

    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasPermission(null, 'MOVIE_UPDATE')")
    public ResponseEntity<MovieResDTO> changeStatus(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(movieService.changeStatus(id));
    }

    @GetMapping("/fetch/{id}")
    @ApiMessage("Fetched movie")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public ResponseEntity<MovieResDTO> findById(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(movieService.getById(id));
    }

    @GetMapping("/active")
    @ApiMessage("Fetched all active movies")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public ResponseEntity<List<MovieResDTO>> findAllActive() {
        return ResponseEntity.ok(movieService.getAllActive());
    }

    @GetMapping("/ids")
    @ApiMessage("Fetched movies by ids")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public ResponseEntity<List<MovieResDTO>> findByIds(@RequestParam("ids") List<Long> ids) throws IdInvalidException {
        return ResponseEntity.ok(movieService.getByIds(ids));
    }

    @GetMapping("/search")
    @ApiMessage("Fetched movies by title")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public ResponseEntity<List<MovieResDTO>> searchByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(movieService.searchByTitle(title));
    }

    @GetMapping("/showing")
    @ApiMessage("Fetched movies now showing")
    public ResponseEntity<List<MovieResDTO>> fetchShowingMovies
            (@RequestParam(value = "limit", defaultValue = "8") int limit) {
        return ResponseEntity.ok(movieService.fetchShowingMovies(limit));
    }

    @GetMapping("/coming-soon")
    @ApiMessage("Fetched movies coming soon")
    public ResponseEntity<List<MovieResDTO>> fetchComingSoonMovies(
            @RequestParam(value = "limit", defaultValue = "8") int limit) {
        return ResponseEntity.ok(movieService.getComingSoonMovies(limit));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'MOVIE_UPDATE')")
    public ResponseEntity<MovieResDTO> update(@PathVariable("id") Long id, MovieReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(movieService.updateMovie(id, dto));
    }

    @Override
    public ResponseEntity<Movie> getById(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("GET /fetch/{id} is supported!");
    }

    @GetMapping("/exists/{id}")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public boolean isExistsById(@PathVariable("id") Long id) {
        return movieService.existsById(id);
    }
}
