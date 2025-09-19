package com.example.service.impl;

import com.example.domain.entity.*;
import com.example.domain.request.ReqSeatDTO;
import com.example.domain.response.ResSeatDTO;
import com.example.mapper.SeatMapper;
import com.example.repository.RoomRepository;
import com.example.repository.SeatRepository;
import com.example.repository.SeatTypeRepository;
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
    private final SeatMapper seatMapper;

    public SeatServiceImpl(RoomRepository roomRepository,
                           SeatRepository seatRepository,
                           SeatMapper seatMapper,
                           SeatTypeRepository seatTypeRepository) {
        super(seatRepository);
        this.roomRepository = roomRepository;
        this.seatMapper = seatMapper;
        this.seatRepository = seatRepository;
        this.seatTypeRepository = seatTypeRepository;
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
        assert nextSeat != null;
        if (nextSeat.getSeatType().getName().equals("Đôi") && seatType.getName().equals("Đôi")) {
            throw new IdInvalidException("Ghế bên cạnh đang là ghế đôi, hãy chuyển sang loại khác trước!");
        }
        Room room = this.roomRepository.findById(dto.getRoomId()).
                orElseThrow(() -> new IdInvalidException("Room không tồn tại!"));

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
            dto.setSeatType(seat.getSeatType());
            return dto;
        }).collect(Collectors.toList());
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
