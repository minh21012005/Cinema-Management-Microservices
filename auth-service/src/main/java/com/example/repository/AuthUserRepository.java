package com.example.repository;

import com.example.domain.entity.AuthUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends BaseRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByRefreshTokenAndEmail(String token, String email);
    boolean existsByEmail(String email);
}

