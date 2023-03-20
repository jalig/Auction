package com.skypro.auction.repository;

import com.skypro.auction.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    @Query(
            value = "SELECT bidder_name AS bidderName, bid_date AS bidDate FROM bid AS b WHERE b ORDER BY bid_date LIMIT 1"
            , nativeQuery = true
    )
    Optional<Bid> findInfoAboutFirstBidder(Long id);

    @Query(
            value = "SELECT bidder_name AS bidderName, MAX(b.bids) AS max_bids, MAX(last_bid_date) AS bidDate " +
            "FROM (SELECT bidder_name, COUNT(*) AS bids, MAX(bid_date) AS last_bid_date " +
            "FROM bid WHERE lot_id = ?1 GROUP BY bidder_name) AS b " +
            "GROUP BY bidder_name ORDER BY max_bids DESC LIMIT 1",
            nativeQuery = true
    )
    Optional<Bid> findHighestNumberOfBets(Long id);

    @Query(
            value = "SELECT COUNT(*) FROM bid WHERE lot_id = ?1"
            , nativeQuery = true
    )
    Long bidCount(Long id);

    @Query(
            value = "SELECT bidder_name AS bidderName, bid_date AS bidDate FROM bid WHERE lot_id = ?1 ORDER BY bid_date DESC LIMIT 1"
            , nativeQuery = true
    )
    Bid getInfoAboutLastBidDate(Long id);
}

