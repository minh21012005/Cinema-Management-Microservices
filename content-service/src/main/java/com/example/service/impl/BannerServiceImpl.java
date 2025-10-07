package com.example.service.impl;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.mapper.BannerMapper;
import com.example.repository.BannerRepository;
import com.example.service.BannerService;
import com.example.util.error.IdInvalidException;
import org.springframework.stereotype.Service;

@Service
public class BannerServiceImpl
        extends BaseServiceImpl<Banner, Long, BannerReqDTO, BannerResDTO>
        implements BannerService {

    private final BannerRepository bannerRepository;
    private final BannerMapper bannerMapper;


    protected BannerServiceImpl(BannerRepository bannerRepository,
                                BannerMapper bannerMapper) {
        super(bannerRepository);
        this.bannerMapper = bannerMapper;
        this.bannerRepository = bannerRepository;
    }

    @Override
    public BannerResDTO createBanner(BannerReqDTO dto) throws IdInvalidException {

        // --- Business Validation ---
        // 1. Ngày kết thúc phải sau ngày bắt đầu
        if (dto.getStartDate() != null && dto.getEndDate() != null &&
                dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IdInvalidException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        // 2. Kiểm tra displayOrder trùng với banner active khác
        if (bannerRepository.existsByDisplayOrderAndActiveTrue(dto.getDisplayOrder())) {
            throw new IdInvalidException("Thứ tự hiển thị đã tồn tại cho banner đang active");
        }

        // 3. Redirect URL nếu muốn có format hợp lệ (ví dụ http/https)
        if (dto.getRedirectUrl() != null && !dto.getRedirectUrl().isEmpty()) {
            if (!dto.getRedirectUrl().matches("^(https?://).+")) {
                throw new IdInvalidException("Redirect URL phải bắt đầu bằng http:// hoặc https://");
            }
        }

        // --- Build Entity ---
        Banner banner = bannerMapper.toEntity(dto);

        // --- Save ---
        Banner saved = bannerRepository.save(banner);

        // --- Return DTO ---
        return bannerMapper.toDto(saved);
    }

}
