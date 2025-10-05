package com.example.repository;

import com.example.domain.entity.SePayTransaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SepayRepository extends BaseRepository<SePayTransaction, Long>, JpaSpecificationExecutor<SePayTransaction> {
}
