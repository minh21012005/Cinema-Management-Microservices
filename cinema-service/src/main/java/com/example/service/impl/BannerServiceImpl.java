package com.example.service.impl;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.mapper.BannerMapper;
import com.example.repository.BannerRepository;
import com.example.service.BannerService;
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
}
