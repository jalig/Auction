package com.skypro.auction.service;

import com.skypro.auction.dto.CreateLot;
import com.skypro.auction.dto.FullLot;
import com.skypro.auction.model.Lot;
import com.skypro.auction.repository.BidRepository;
import com.skypro.auction.repository.LotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LotService {


    private final LotRepository lotRepository;
    private final BidRepository bidRepository;

    private final Logger logger = LoggerFactory.getLogger(Lot.class);

    public LotService(LotRepository lotRepository, BidRepository bidRepository) {
        this.lotRepository = lotRepository;
        this.bidRepository = bidRepository;
    }


    public CreateLot createLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        Lot createdLot = lotRepository.save(lot);
        createdLot.setStatus("CREATED");
        logger.info("Created lot: {} ", lot);
        return CreateLot.fromLot(createdLot);
    }

    public FullLot findLotById(Long id) {
        logger.info("Find lot with id {}", id);
        return FullLot.fromLot(lotRepository.findById(id).get());
    }

    public CreateLot createStartedLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        Lot createdLot = lotRepository.save(lot);
        createdLot.setStatus("STARTED");
        logger.info("Created lot: {} ", lot);
        return CreateLot.fromLot(createdLot);
    }

    public FullLot createBidLot(FullLot fullLot) { //переделать, ничего не понимаю)
        Lot lot = fullLot.toLot();
        Lot createdLot = lotRepository.save(lot);
        createdLot.setStatus("CREATED");
        logger.info("Created lot: {} ", lot);
        return FullLot.fromLot(createdLot);
    }
    public CreateLot createStopLot(CreateLot createLot) {
        Lot lot = createLot.toLot();
        Lot createdLot = lotRepository.save(lot);
        createdLot.setStatus("STOPPED");
        logger.info("Created lot: {} ", lot);
        return CreateLot.fromLot(createdLot);
    }

    public List<CreateLot> findLotsByStatus(String status, Pageable pageable) {
        logger.info("Found all lots where status: {}", status);
        return lotRepository.findAllByStatusContainingIgnoreCase(status, pageable)
                .stream()
                .map(CreateLot::fromLot)
                .collect(Collectors.toList());
    }


    //Метод с экспортом в csv напишу как пойму что не тупой


}
