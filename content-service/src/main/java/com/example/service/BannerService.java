package com.example.service;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BannerService extends BaseService<Banner, Long, BannerReqDTO, BannerResDTO> {
    BannerResDTO createBanner(BannerReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO fetchAllBanners(String title, Pageable pageable);
    List<BannerResDTO> fetchAllBannersActive();
    BannerResDTO updateBanner(Long id, BannerReqDTO dto) throws IdInvalidException;
}
