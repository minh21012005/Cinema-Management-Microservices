package com.example.repository;

import com.example.domain.entity.SupportMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportMessageRepository extends BaseRepository<SupportMessage, Long> {
}
