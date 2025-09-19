package com.example.controller;

import com.example.domain.entity.Cinema;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.CinemaMapper;
import com.example.service.CinemaService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cinemas")
public class CinemaController extends BaseController<Cinema, Long, CinemaReqDTO, CinemaResDTO> {

    private final CinemaService cinemaService;
    private final CinemaMapper cinemaMapper;

    protected CinemaController(CinemaService cinemaService, CinemaMapper cinemaMapper) {
        super(cinemaService, cinemaMapper);
        this.cinemaService = cinemaService;
        this.cinemaMapper = cinemaMapper;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'CINEMA_CREATE')")
    public ResponseEntity<CinemaResDTO> create(CinemaReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(cinemaService.createCinema(dto));
    }

    @GetMapping("/fetch-all")
    @ApiMessage("Fetched all cinema")
    @PreAuthorize("hasPermission(null, 'CINEMA_VIEW_ALL')")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(name = "name", required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(cinemaService.fetchAllCinema(name, pageable));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'CINEMA_VIEW')")
    public ResponseEntity<Cinema> getById(@PathVariable("id") Long id) throws IdInvalidException {
        return super.getById(id);
    }

    @Override
    @PreAuthorize("hasPermission(null, 'CINEMA_UPDATE')")
    public ResponseEntity<CinemaResDTO> update(
            @PathVariable("id") Long id, CinemaReqDTO dto) throws IdInvalidException {
        Cinema cinema = cinemaService.findById(id).orElse(null);
        if (cinema != null) {
            if(!cinema.getName().equals(dto.getName()) && cinemaService.existsByName(dto.getName())){
                throw new IdInvalidException("Name is already existed!");
            }
            if(!cinema.getPhone().equals(dto.getPhone()) && cinemaService.existsByPhone(dto.getPhone())){
                throw new IdInvalidException("Phone is already existed!");
            }
            if(!cinema.getAddress().equals(dto.getAddress()) && cinemaService.existsByAddress(dto.getAddress())){
                throw new IdInvalidException("Address is already existed!");
            }
        }
        return super.update(id, dto);
    }

    @PutMapping("/change-status/{id}")
    @ApiMessage("Changed status of cinema")
    @PreAuthorize("hasPermission(null, 'CINEMA_UPDATE')")
    public ResponseEntity<Object> changeStatus(@PathVariable("id") Long id) {
        Cinema cinema = this.cinemaService.changeStatusOfCinema(id);
        return ResponseEntity.status(HttpStatus.OK).body(cinema);
    }

    @Override
    public ResponseEntity<Void> delete(Long aLong) throws IdInvalidException {
        throw new UnsupportedOperationException("Delete cinema is not supported!");
    }

    @Override
    public ResponseEntity<List<Cinema>> getAll() {
        throw new UnsupportedOperationException("GET /fetch-all is supported!");
    }
}
