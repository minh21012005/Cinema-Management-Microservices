package com.example.controller;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.mapper.BannerMapper;
import com.example.service.BannerService;
import com.example.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
public class BannerController extends BaseController<Banner, Long, BannerReqDTO, BannerResDTO> {

    private final BannerService bannerService;

    protected BannerController(BannerService bannerService, BannerMapper bannerMapper) {
        super(bannerService, bannerMapper);
        this.bannerService = bannerService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'FOOD_CREATE')")
    public ResponseEntity<BannerResDTO> create(BannerReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerService.createBanner(dto));
    }

}
