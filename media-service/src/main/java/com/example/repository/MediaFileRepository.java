package com.example.repository;

import com.example.domain.entity.MediaFile;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepository extends BaseRepository<MediaFile, Long> {
}
