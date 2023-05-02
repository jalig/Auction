package com.skypro.auction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skypro.auction.model.Bid;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BidDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String bidderName;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime bidDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lotId;

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setId(this.getId());
        bid.setBidderName(this.getBidderName());
        return bid;
    }


}
