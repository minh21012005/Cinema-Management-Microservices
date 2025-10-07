package com.example.repository;

import com.example.domain.entity.Banner;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends BaseRepository<Banner, Long>, JpaSpecificationExecutor<Banner> {
}
