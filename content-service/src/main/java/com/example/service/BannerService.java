package com.example.service;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.util.error.IdInvalidException;

public interface BannerService extends BaseService<Banner, Long, BannerReqDTO, BannerResDTO> {
    BannerResDTO createBanner(BannerReqDTO dto) throws IdInvalidException;
}
