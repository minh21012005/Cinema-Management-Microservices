package com.example.controller;

import com.example.domain.entity.Movie;
import com.example.domain.request.MovieReqDTO;
import com.example.domain.response.MovieResDTO;
import com.example.mapper.MovieMapper;
import com.example.service.MovieService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController extends BaseController<Movie, Long, MovieReqDTO, MovieResDTO> {

    private final MovieService movieService;

    protected MovieController(MovieService movieService, MovieMapper movieMapper) {
        super(movieService, movieMapper);
        this.movieService = movieService;
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


    @Override
    public ResponseEntity<Movie> getById(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException("GET /fetch/{id} is supported!");
    }

    @GetMapping("/exists/{id}")
    @PreAuthorize("hasPermission(null, 'MOVIE_VIEW')")
    public boolean isExistsById(@PathVariable("id") Long id){
        return movieService.existsById(id);
    }
}
