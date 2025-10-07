package com.example.controller;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.BannerMapper;
import com.example.service.BannerService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
public class BannerController extends BaseController<Banner, Long, BannerReqDTO, BannerResDTO> {

    private final BannerService bannerService;

    protected BannerController(BannerService bannerService, BannerMapper bannerMapper) {
        super(bannerService, bannerMapper);
        this.bannerService = bannerService;
    }

    @GetMapping("/all")
    @ApiMessage("Fetched all banners")
    @PreAuthorize("hasPermission(null, 'BANNER_VIEW')")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(name = "title", required = false) String title,
            Pageable pageable) {
        return ResponseEntity.ok(bannerService.fetchAllBanners(title, pageable));
    }

    @GetMapping("/active")
    @ApiMessage("Fetched all foods active")
    public ResponseEntity<List<BannerResDTO>> getAllFoodsActive() {
        return ResponseEntity.ok(bannerService.fetchAllBannersActive());
    }

    @Override
    @PreAuthorize("hasPermission(null, 'BANNER_CREATE')")
    public ResponseEntity<BannerResDTO> create(BannerReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerService.createBanner(dto));
    }

}
