package com.example.service.impl;

import com.example.domain.entity.*;
import com.example.domain.request.RoomReqCreateDTO;
import com.example.domain.request.RoomReqDTO;
import com.example.domain.response.RoomResDTO;
import com.example.mapper.RoomMapper;
import com.example.repository.RoomRepository;
import com.example.repository.RoomTypeRepository;
import com.example.repository.SeatTypeRepository;
import com.example.service.CinemaService;
import com.example.service.RoomService;
import com.example.util.error.IdInvalidException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl
        extends BaseServiceImpl<Room, Long, RoomReqDTO, RoomResDTO>
        implements RoomService {
    private final RoomRepository roomRepository;
    private final CinemaService cinemaService;
    private final RoomMapper roomMapper;
    private final RoomTypeRepository roomTypeRepository;
    private final SeatTypeRepository seatTypeRepository;

    protected RoomServiceImpl(RoomRepository roomRepository,
                              RoomMapper roomMapper,
                              RoomTypeRepository roomTypeRepository,
                              CinemaService cinemaService,
                              SeatTypeRepository seatTypeRepository) {
        super(roomRepository);
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomTypeRepository = roomTypeRepository;
        this.seatTypeRepository = seatTypeRepository;
        this.cinemaService = cinemaService;
    }

    @Override
    public List<RoomResDTO> fetchAllRoom(Long cinemaId) {
        List<Room> rooms = this.roomRepository.findByCinema_Id(cinemaId);

        if (rooms.isEmpty()) {
            return Collections.emptyList(); // trả về list rỗng, không null
        }

        return rooms.stream().map(roomMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RoomType> fetchAllRoomType() {
        return roomTypeRepository.findAll();
    }

    @Override
    public RoomResDTO createRoom(RoomReqCreateDTO dto) throws IdInvalidException {
        // 1. Lấy Cinema
        Cinema cinema = cinemaService.findById(dto.getCinemaId())
                .orElseThrow(() -> new IdInvalidException("Cinema not found with id = " + dto.getCinemaId()));

        if (!cinema.isActive()) {
            throw new IllegalArgumentException("Cinema đang không hoạt động!");
        }

        boolean exists = roomRepository.existsByNameAndCinema(dto.getName(), cinema);
        if (exists) {
            throw new IdInvalidException("Room name '" + dto.getName() + "' already exists in this cinema");
        }

        // 2. Lấy RoomType
        RoomType roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new IdInvalidException("RoomType not found with id = " + dto.getRoomTypeId()));

        // 3. Khởi tạo Room
        Room room = Room.builder()
                .name(dto.getName())
                .cinema(cinema)
                .roomType(roomType)
                .active(true)
                .build();

        // 4. Map Seats từ DTO
        List<Seat> seats = dto.getSeats().stream().map(seatDto -> {
            SeatType seatType = seatTypeRepository.findById(seatDto.getType())
                    .orElseThrow(() -> new RuntimeException("SeatType not found with id = " + seatDto.getType()));

            return Seat.builder()
                    .rowIndex(seatDto.getRow())
                    .colIndex(seatDto.getCol())
                    .name(seatDto.getName())
                    .active(true)
                    .seatType(seatType)
                    .room(room) // gắn room
                    .build();
        }).toList();

        room.setSeats(seats);

        // 5. Save room (cascade -> save seats)
        return roomMapper.toDto(roomRepository.save(room));
    }

    @Override
    public RoomResDTO updateRoom(Long id, RoomReqDTO dto) throws IdInvalidException {
        Optional<Room> roomOptional = this.roomRepository.findById(id);
        Room room = roomOptional.orElseThrow(
                () -> new IdInvalidException("Room không tồn tại trong hệ thống!")
        );

        Cinema cinemaDb = room.getCinema();

        if (!cinemaDb.isActive()) {
            throw new IllegalArgumentException("Cinema đang không hoạt động!");
        }

        boolean isNameExist = false;
        if (!dto.getName().equals(room.getName())) {
            Cinema cinema = room.getCinema();
            isNameExist = cinema.getRooms().stream().anyMatch(r -> r.getName().equals(dto.getName()));
        }
        if (isNameExist) {
            throw new IdInvalidException("Tên phòng đã tồn tại!");
        }
        room.setName(dto.getName());
        room.setRoomType(this.roomTypeRepository.findById(dto.getTypeId()).get());
        Room saved = this.roomRepository.save(room);
        return roomMapper.toDto(saved);

    }

    @Override
    @Transactional
    public RoomResDTO changeStatus(Long id) throws IdInvalidException {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Room không tồn tại trong hệ thống!")
        );

        Cinema cinemaDb = room.getCinema();
        if (!cinemaDb.isActive()) {
            throw new IllegalArgumentException("Cinema đang không hoạt động!");
        }

        boolean newStatus = !room.isActive();
        room.setActive(newStatus);

        // Nếu tắt room
        if (!newStatus) {
            // Tắt tất cả seat
            room.getSeats().forEach(seat -> seat.setActive(false));

            // Tắt tất cả showtime
            room.getShowtimes().forEach(showtime -> showtime.setActive(false));
        }
        return roomMapper.toDto(roomRepository.save(room));
    }

}
