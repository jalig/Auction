package com.skypro.auction.dto;

import com.skypro.auction.model.Bid;
import com.skypro.auction.model.Lot;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidDTO {
    private String bidderName;
    private LocalDateTime bidDate;
    private Long lotId;


    public static BidDTO fromBid(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        Lot lot = new Lot();
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());
        bidDTO.setLotId(lot.getId());
        return bidDTO;
    }

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setBidderName(this.getBidderName());
        bid.setBidDate(this.getBidDate());
        return bid;
    }


}
