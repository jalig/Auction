package com.skypro.auction.repository;

import com.skypro.auction.model.Lot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    Page<Lot> findAllByStatus(String status, Pageable pageable);
}
