package com.skypro.auction.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bidderName;
    private LocalDateTime bidDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Lot lot;

}
