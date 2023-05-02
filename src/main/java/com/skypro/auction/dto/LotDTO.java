package com.skypro.auction.dto;

import com.skypro.auction.enums.Status;
import com.skypro.auction.model.Lot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class LotDTO {
    private Long id;
    private Status status;
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidPrice;

    public static LotDTO fromLot(Lot lot) {
        LotDTO lotDTO = new LotDTO();
        BeanUtils.copyProperties(lot, lotDTO);
        return lotDTO;
    }
}
