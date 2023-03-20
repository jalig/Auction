package com.skypro.auction.dto;

import com.skypro.auction.model.Bid;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BidDTO {
    private Long id;
    private String bidderName;
    private LocalDateTime bidDate;


    public static BidDTO fromBid(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setBidderName(bid.getBidderName());
        bidDTO.setBidDate(bid.getBidDate());
        return bidDTO;
    }

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setId(this.getId());
        bid.setBidderName(this.getBidderName());
        bid.setBidDate(this.getBidDate());
        return bid;
    }


}
