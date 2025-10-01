package com.example.repository;

import com.example.domain.entity.Ticket;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends BaseRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    List<Ticket> findByShowtimeIdAndPaidTrue(Long showtimeId);
}
