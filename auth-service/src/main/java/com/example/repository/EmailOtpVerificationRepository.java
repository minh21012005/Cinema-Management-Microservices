package com.example.repository;

import com.example.domain.entity.EmailOtpVerification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailOtpVerificationRepository extends BaseRepository<EmailOtpVerification, Long> {
    @Query("""
                SELECT e FROM EmailOtpVerification e
                WHERE e.email = :email
                  AND e.otp = :otp
                  AND e.verified = false
                  AND e.expiredAt > :now
            """)
    Optional<EmailOtpVerification> findValidToken(
            @Param("email") String email,
            @Param("otp") String otp,
            @Param("now") LocalDateTime now
    );
}
