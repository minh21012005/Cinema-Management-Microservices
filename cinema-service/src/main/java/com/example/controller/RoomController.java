package com.example.controller;

import com.example.domain.entity.Cinema;
import com.example.domain.entity.Room;
import com.example.domain.entity.RoomType;
import com.example.domain.request.RoomReqCreateDTO;
import com.example.domain.request.RoomReqDTO;
import com.example.domain.response.RoomResDTO;
import com.example.mapper.RoomMapper;
import com.example.service.CinemaService;
import com.example.service.RoomService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController extends BaseController<Room, Long, RoomReqDTO, RoomResDTO> {

    private final RoomService roomService;
    private final CinemaService cinemaService;

    protected RoomController(RoomService roomService, RoomMapper roomMapper, CinemaService cinemaService) {
        super(roomService, roomMapper);
        this.roomService = roomService;
        this.cinemaService = cinemaService;
    }

    @GetMapping("/cinemas/{id}")
    @PreAuthorize("hasPermission(null, 'ROOM_VIEW')")
    public ResponseEntity<List<RoomResDTO>> getRoomsByCinema(@PathVariable("id") Long id)
            throws IdInvalidException {
        Optional<Cinema> cinema = this.cinemaService.findById(id);
        if (cinema.isEmpty()) {
            throw new IdInvalidException("Cinema với id " + id + " không tồn tại!");
        }

        List<RoomResDTO> rooms = this.roomService.fetchAllRoom(id);
        return ResponseEntity.ok(rooms); // nếu rooms rỗng thì trả [] thay vì null
    }

    @GetMapping("/types")
    public ResponseEntity<List<RoomType>> fetchAllRoomType() {
        return ResponseEntity.status(HttpStatus.OK).body(this.roomService.fetchAllRoomType());
    }

    @PostMapping("/create")
    @PreAuthorize("hasPermission(null, 'ROOM_CREATE')")
    public ResponseEntity<RoomResDTO> createRoom(@RequestBody RoomReqCreateDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roomService.createRoom(dto));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROOM_UPDATE')")
    public ResponseEntity<RoomResDTO> update(
            @PathVariable("id") Long id, @RequestBody RoomReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(roomService.updateRoom(id, dto));
    }

    @PutMapping("/change-status/{id}")
    @PreAuthorize("hasPermission(null, 'ROOM_UPDATE')")
    public ResponseEntity<RoomResDTO> changeRoomStatus(@PathVariable("id") Long id) throws IdInvalidException {
        return ResponseEntity.ok(roomService.changeStatus(id));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROOM_VIEW')")
    public ResponseEntity<Room> getById(@PathVariable("id") Long id) throws IdInvalidException {
        return super.getById(id);
    }

    @Override
    public ResponseEntity<RoomResDTO> create(@RequestBody RoomReqDTO dto) {
        throw new UnsupportedOperationException("Post /create is supported!");
    }

    @Override
    @PreAuthorize("hasPermission(null, 'ROOM_VIEW')")
    public ResponseEntity<List<Room>> getAll() {
        return super.getAll();
    }

    @Override
    public ResponseEntity<Void> delete(Long aLong) throws IdInvalidException {
        throw new UnsupportedOperationException("Delete room is not supported!");
    }
}
