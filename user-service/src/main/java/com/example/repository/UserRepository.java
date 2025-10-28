package com.example.repository;

import com.example.domain.entity.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<User> findAllByAuthIdIn(List<Long> authIds);

    Long countByCreatedAtAfter(LocalDateTime dateTime);
}
