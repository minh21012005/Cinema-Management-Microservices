package com.example.service.impl;

import com.example.client.BookingClient;
import com.example.domain.entity.*;
import com.example.domain.request.ReqSeatDTO;
import com.example.domain.response.ResSeatDTO;
import com.example.domain.response.SeatStatusDTO;
import com.example.mapper.SeatMapper;
import com.example.repository.RoomRepository;
import com.example.repository.SeatRepository;
import com.example.repository.SeatTypeRepository;
import com.example.repository.ShowtimeRepository;
import com.example.service.SeatService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatServiceImpl
        extends BaseServiceImpl<Seat, Long, ReqSeatDTO, ResSeatDTO>
        implements SeatService {
    private final RoomRepository roomRepository;
    private final SeatRepository seatRepository;
    private final SeatTypeRepository seatTypeRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatMapper seatMapper;
    private final BookingClient bookingClient;

    public SeatServiceImpl(RoomRepository roomRepository,
                           SeatRepository seatRepository,
                           SeatMapper seatMapper,
                           ShowtimeRepository showtimeRepository,
                           SeatTypeRepository seatTypeRepository,
                           BookingClient bookingClient) {
        super(seatRepository);
        this.roomRepository = roomRepository;
        this.seatMapper = seatMapper;
        this.seatRepository = seatRepository;
        this.seatTypeRepository = seatTypeRepository;
        this.showtimeRepository = showtimeRepository;
        this.bookingClient = bookingClient;
    }

    @Override
    public ResSeatDTO createSeat(ReqSeatDTO dto) throws IdInvalidException {
        Seat rightSeat = this.seatRepository.findByRowIndexAndColIndexAndRoomId(
                dto.getRow(), dto.getCol(), dto.getRoomId()).orElse(null);
        Seat prevSeat = this.seatRepository.findByRowIndexAndColIndexAndRoomId(
                dto.getRow(), dto.getCol() - 1, dto.getRoomId()).orElse(null);
        Seat nextSeat = this.seatRepository.findByRowIndexAndColIndexAndRoomId(
                dto.getRow(), dto.getCol() + 1, dto.getRoomId()).orElse(null);
        SeatType seatType = this.findSeatTypeById(dto.getTypeId())
                .orElseThrow(() -> new IdInvalidException("Loại ghế không tồn tại!"));

        if (rightSeat != null) {
            throw new IdInvalidException("Vị trí này đã có ghế, không thể tạo!");
        }
        if (prevSeat != null && prevSeat.getSeatType().getName().equals("Đôi")) {
            throw new IdInvalidException("Vị trí này đã có ghế, không thể tạo!");
        }
        if (nextSeat != null && nextSeat.isActive() && seatType.getName().equals("Đôi")) {
            throw new IdInvalidException("Vị trí này đã có ghế, không thể tạo!");
        }
        if (nextSeat != null &&
                nextSeat.getSeatType().getName().equals("Đôi") &&
                seatType.getName().equals("Đôi")) {
            throw new IdInvalidException("Ghế bên cạnh đang là ghế đôi, hãy chuyển sang loại khác trước!");
        }

        Room room = this.roomRepository.findById(dto.getRoomId()).
                orElseThrow(() -> new IdInvalidException("Room không tồn tại!"));

        if (!room.isActive()) {
            throw new IllegalArgumentException("Room đang không hoạt động!");
        }

        Seat seat = getSeat(dto, seatType, room);
        return seatMapper.toDto(seatRepository.save(seat));
    }

    @Override
    public List<SeatType> fetchAllSeatTypes() {
        return this.seatTypeRepository.findAll();
    }

    @Override
    public List<ResSeatDTO> fetchSeatsByRoom(Long id) throws IdInvalidException {
        Optional<Room> roomOptional = this.roomRepository.findById(id);
        if (roomOptional.isEmpty()) {
            throw new IdInvalidException("Room không tồn tại!");
        }
        List<Seat> seats = roomOptional.get().getSeats();
        return seats.stream().map(seat -> {
            ResSeatDTO dto = new ResSeatDTO();
            dto.setId(seat.getId());
            dto.setName(seat.getName());
            dto.setActive(seat.isActive());
            dto.setRowIndex(seat.getRowIndex());
            dto.setColIndex(seat.getColIndex());
            dto.setSeatType(seat.getSeatType());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ResSeatDTO changeSeatStatus(Long id) throws IdInvalidException {
        Seat seat = seatRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Seat không tồn tại trong hệ thống!")
        );
        Room room = seat.getRoom();
        if (!room.isActive()) {
            throw new IllegalArgumentException("Room đang không hoạt động!");
        }
        seat.setActive(!seat.isActive());
        return seatMapper.toDto(seatRepository.save(seat));
    }

    @Override
    public ResSeatDTO changeSeatType(Long id, Long typeId) throws IdInvalidException {
        Seat seat = seatRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Seat không tồn tại trong hệ thống!")
        );
        SeatType seatType = seatTypeRepository.findById(typeId).orElseThrow(
                () -> new IdInvalidException("Seat Type không tồn tại trong hệ thống!")
        );

        Room room = seat.getRoom();
        if (!room.isActive()) {
            throw new IllegalArgumentException("Room đang không hoạt động!");
        }

        if (!seat.getSeatType().getName().equals("Đôi")) {
            if (!seatType.getName().equals("Đôi")) {
                seat.setSeatType(seatType);
            } else {
                int row = seat.getRowIndex();
                int col = seat.getColIndex();
                Long roomId = seat.getRoom().getId();
                Seat nextSeat = seatRepository.findByRowIndexAndColIndexAndRoomId
                        (row, col + 1, roomId).orElse(null);
                if (nextSeat != null && nextSeat.getSeatType().getName().equals("Đôi")) {
                    throw new IdInvalidException("Ghế bên cạnh đang là ghế đôi, hãy chuyển sang loại khác trước!");
                }
                if (nextSeat == null || !nextSeat.isActive()) {
                    seat.setName(seat.getName() + "-" + (col + 2));
                    seat.setSeatType(seatType);
                } else {
                    throw new IdInvalidException("Ghế bên cạnh đang hoạt động, không thể đổi sang loại ghế đôi!");
                }
            }
        } else {
            if (!seatType.getName().equals("Đôi")) {
                int index = seat.getName().indexOf("-");
                seat.setName(seat.getName().substring(0, index));
            }
            seat.setSeatType(seatType);
        }
        return seatMapper.toDto(seatRepository.save(seat));
    }

    @Override
    public List<SeatStatusDTO> fetchSeatsByShowtime(Long id) throws IdInvalidException {
        Showtime showtime = showtimeRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Showtime không tồn tại!")
        );

        Room room = showtime.getRoom();
        if(room == null){
            throw new IdInvalidException("Room không tồn tại!");
        }

        // 2. Lấy danh sách ghế active
        List<Seat> activeSeats = room.getSeats().stream()
                .filter(Seat::isActive) // chỉ lấy ghế active
                .toList();
        List<Long> bookedSeatIds = bookingClient.getBookedSeatIds(id).getData();

        return activeSeats.stream()
                .map(seat -> SeatStatusDTO.builder()
                        .id(seat.getId())
                        .name(seat.getName())
                        .rowIndex(seat.getRowIndex())
                        .colIndex(seat.getColIndex())
                        .seatType(seat.getSeatType())
                        .booked(bookedSeatIds.contains(seat.getId())) // ghế đã đặt
                        .build())
                .toList();
    }

    public Optional<SeatType> findSeatTypeById(Long id) {
        return this.seatTypeRepository.findById(id);
    }

    private static Seat getSeat(ReqSeatDTO dto, SeatType seatType, Room room) {
        char rowChar = (char) ('A' + dto.getRow());

        int colNumber = dto.getCol() + 1; // vì col bắt đầu từ 0

        String seatName;
        if (seatType.getName().equals("Đôi")) {
            seatName = rowChar + String.valueOf(colNumber) + "-" + (colNumber + 1);
        } else {
            seatName = rowChar + String.valueOf(colNumber);
        }

        Seat seat = new Seat();
        seat.setRowIndex(dto.getRow());
        seat.setColIndex(dto.getCol());
        seat.setSeatType(seatType);
        seat.setRoom(room);
        seat.setName(seatName);
        return seat;
    }
}
