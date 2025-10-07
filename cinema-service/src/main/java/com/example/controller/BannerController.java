package com.example.controller;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.mapper.BannerMapper;
import com.example.service.BannerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/banners")
public class BannerController extends BaseController<Banner, Long, BannerReqDTO, BannerResDTO> {

    private final BannerService bannerService;

    protected BannerController(BannerService bannerService, BannerMapper bannerMapper) {
        super(bannerService, bannerMapper);
        this.bannerService = bannerService;
    }


}
