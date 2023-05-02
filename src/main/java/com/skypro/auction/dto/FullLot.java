package com.skypro.auction.dto;

import com.skypro.auction.enums.Status;
import com.skypro.auction.model.Lot;
import com.skypro.auction.projection.BidView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FullLot {

    private Long id;
    private Status status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer currentPrice;
    private Integer bidPrice;
    private BidView lastBid;

    public static FullLot fromLot(Lot lot) {
        FullLot fullLot = new FullLot();
        fullLot.setId(lot.getId());
        fullLot.setStatus(lot.getStatus());
        fullLot.setTitle(lot.getTitle());
        fullLot.setDescription(lot.getDescription());
        fullLot.setStartPrice(lot.getStartPrice());
        fullLot.setBidPrice(lot.getBidPrice());
        return fullLot;
    }

}

