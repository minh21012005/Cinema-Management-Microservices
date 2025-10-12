package com.example.repository;

import com.example.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUserId(Long userId);
}
