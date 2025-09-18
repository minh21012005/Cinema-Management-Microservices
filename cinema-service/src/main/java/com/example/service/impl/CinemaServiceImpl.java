package com.example.service.impl;

import com.example.domain.entity.Cinema;
import com.example.domain.request.CinemaReqDTO;
import com.example.domain.response.CinemaResDTO;
import com.example.mapper.CinemaMapper;
import com.example.repository.CinemaRepository;
import com.example.service.CinemaService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

@Service
public class CinemaServiceImpl
        extends BaseServiceImpl<Cinema, Long, CinemaReqDTO, CinemaResDTO>
        implements CinemaService {
    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;

    protected CinemaServiceImpl(CinemaRepository cinemaRepository,
                                CinemaMapper cinemaMapper) {
        super(cinemaRepository);
        this.cinemaRepository = cinemaRepository;
        this.cinemaMapper = cinemaMapper;
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
}
