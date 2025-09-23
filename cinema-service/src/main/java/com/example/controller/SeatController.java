package com.example.controller;

import com.example.domain.entity.Seat;
import com.example.domain.entity.SeatType;
import com.example.domain.request.ReqSeatDTO;
import com.example.domain.response.ResSeatDTO;
import com.example.mapper.SeatMapper;
import com.example.service.SeatService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatController extends BaseController<Seat, Long, ReqSeatDTO, ResSeatDTO> {

    private final SeatService seatService;
    private final SeatMapper seatMapper;

    protected SeatController(SeatService seatService, SeatMapper seatMapper) {
        super(seatService, seatMapper);
        this.seatService = seatService;
        this.seatMapper = seatMapper;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SEAT_CREATE')")
    public ResponseEntity<ResSeatDTO> create(@RequestBody ReqSeatDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(seatService.createSeat(dto));
    }

    @GetMapping("/types")
    public ResponseEntity<List<SeatType>> fetchAllSeatTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(this.seatService.fetchAllSeatTypes());
    }

    @GetMapping("/rooms/{id}")
    @PreAuthorize("hasPermission(null, 'SEAT_VIEW')")
    public ResponseEntity<List<ResSeatDTO>> fetchAllSeatByRoom(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.OK).body(this.seatService.fetchSeatsByRoom(id));
    }

    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasPermission(null, 'SEAT_UPDATE')")
    public ResponseEntity<ResSeatDTO> changeSeatStatus(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(seatService.changeSeatStatus(id));
    }

    @PutMapping("/{id}/type/{typeId}")
    @PreAuthorize("hasPermission(null, 'SEAT_UPDATE')")
    public ResponseEntity<ResSeatDTO> changeSeatType(
            @PathVariable("id") Long id, @PathVariable("typeId") Long typeId) throws IdInvalidException {
        return ResponseEntity.ok(seatService.changeSeatType(id, typeId));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SEAT_VIEW')")
    public ResponseEntity<List<Seat>> getAll() {
        return super.getAll();
    }

    @Override
    @PreAuthorize("hasPermission(null, 'SEAT_VIEW')")
    public ResponseEntity<Seat> getById(@PathVariable("id") Long id) throws IdInvalidException {
        return super.getById(id);
    }

    @Override
    public ResponseEntity<ResSeatDTO> update(Long aLong, ReqSeatDTO dto) throws IdInvalidException {
        throw new UnsupportedOperationException("Update seat is not supported!");
    }

    @Override
    public ResponseEntity<Void> delete(Long aLong) throws IdInvalidException {
        throw new UnsupportedOperationException("Delete seat is not supported!");
    }
}
