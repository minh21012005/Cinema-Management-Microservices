package com.example.mapper;

import com.example.domain.entity.Banner;
import com.example.domain.request.BannerReqDTO;
import com.example.domain.response.BannerResDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BannerMapper extends BaseMapper<Banner, BannerReqDTO, BannerResDTO> {
}
