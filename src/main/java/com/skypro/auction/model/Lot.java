package com.skypro.auction.model;

import com.skypro.auction.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String title;
    @Column(length = 4096)
    private String description;
    private Integer startPrice;
    private Integer bidPrice;
    @OneToMany(mappedBy = "lot")
    List<Bid> bids;

    public Lot() {
        this.status = Status.CREATED.toString();
    }


}
