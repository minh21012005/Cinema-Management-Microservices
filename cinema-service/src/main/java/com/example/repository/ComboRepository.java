package com.example.repository;

import com.example.domain.entity.Combo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboRepository extends BaseRepository<Combo, Long>, JpaSpecificationExecutor<Combo> {
    boolean existsByCode(String code);
}
