package com.example.repository;

import com.example.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends BaseRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
}
