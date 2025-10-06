package com.example.service.impl;

import com.example.domain.entity.Cinema;
import com.example.domain.entity.Room;
import com.example.domain.entity.Seat;
import com.example.domain.entity.Showtime;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.CinemaMapper;
import com.example.repository.CinemaRepository;
import com.example.repository.ShowtimeRepository;
import com.example.service.CinemaService;
import com.example.service.specification.CinemaSpecification;
import com.example.util.error.IdInvalidException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaServiceImpl
        extends BaseServiceImpl<Cinema, Long, CinemaReqDTO, CinemaResDTO>
        implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;
    private final ShowtimeRepository showtimeRepository;

    protected CinemaServiceImpl(CinemaRepository cinemaRepository,
                                CinemaMapper cinemaMapper,
                                ShowtimeRepository showtimeRepository) {
        super(cinemaRepository);
        this.cinemaRepository = cinemaRepository;
        this.cinemaMapper = cinemaMapper;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public CinemaResDTO createCinema(CinemaReqDTO dto) throws IdInvalidException {
        boolean isNameExists = this.cinemaRepository.existsByName(dto.getName());
        boolean isAddressExists = this.cinemaRepository.existsByAddress(dto.getAddress());
        boolean isPhoneExists = this.cinemaRepository.existsByPhone(dto.getPhone());

        if (isNameExists) {
            throw new IdInvalidException(
                    "Tên " + dto.getName() + " đã tồn tại, vui lòng sử dụng tên khác.");
        }
        if (isAddressExists) {
            throw new IdInvalidException(
                    "Địa chỉ " + dto.getAddress() + " đã tồn tại, vui lòng sử dụng địa chỉ khác.");
        }
        if (isPhoneExists) {
            throw new IdInvalidException(
                    "Số điện thoại " + dto.getPhone() + " đã tồn tại, vui lòng sử dụng số điện thoại khác.");
        }
        Cinema cinema = cinemaMapper.toEntity(dto);
        Cinema saved = cinemaRepository.save(cinema);
        return cinemaMapper.toDto(saved);
    }

    @Override
    public ResultPaginationDTO fetchAllCinema(String name, Pageable pageable) {
        Page<Cinema> pageCinema = this.cinemaRepository.findAll(
                CinemaSpecification.findCinemaWithFilters(name), pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber());
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageCinema.getTotalPages());
        mt.setTotal(pageCinema.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<CinemaResDTO> listCinema = pageCinema.getContent()
                .stream().map(cinemaMapper::toDto)
                .collect(Collectors.toList());

        rs.setResult(listCinema);

        return rs;
    }

    @Override
    public boolean existsByName(String name) {
        return cinemaRepository.existsByName(name);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return cinemaRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByAddress(String address) {
        return cinemaRepository.existsByAddress(address);
    }

    @Override
    @Transactional
    public Cinema changeStatusOfCinema(Long id) throws IdInvalidException {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Cinema không tồn tại trong hệ thống!"));

        boolean newStatus = !cinema.isActive();
        cinema.setActive(newStatus);

        if (!newStatus) {
            // Nếu tắt cinema thì cascade xuống
            for (Room room : cinema.getRooms()) {
                room.setActive(false);
                for (Seat seat : room.getSeats()) {
                    seat.setActive(false);
                }
            }

            // Tắt các suất chiếu trong tương lai
            List<Showtime> showtimes = showtimeRepository.findAllByCinemaId(cinema.getId());
            LocalDateTime now = LocalDateTime.now();
            showtimes.stream()
                    .filter(s -> s.getStartTime().isAfter(now))
                    .forEach(s -> s.setActive(false));
        }

        return cinemaRepository.save(cinema);
    }

    @Override
    public List<CinemaResDTO> fetchActiveCinemas() {
        return cinemaRepository.findByActiveTrue().stream().map(
                cinemaMapper::toDto
        ).toList();
    }

    @Override
    public boolean isExists(Long id) {
        return cinemaRepository.existsById(id);
    }


}
