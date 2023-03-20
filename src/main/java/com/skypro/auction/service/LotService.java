package com.skypro.auction.service;

import com.skypro.auction.dto.BidDTO;
import com.skypro.auction.dto.CreateLot;
import com.skypro.auction.dto.FullLot;
import com.skypro.auction.enums.Status;
import com.skypro.auction.model.Bid;
import com.skypro.auction.model.Lot;
import com.skypro.auction.repository.BidRepository;
import com.skypro.auction.repository.LotRepository;
import liquibase.pro.packaged.F;
import liquibase.pro.packaged.S;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public BidDTO getInfoAboutFirstBidder(Long id) { // Получить информацию о первом ставившем на лот
        return BidDTO.fromBid(bidRepository.findInfoAboutFirstBidder(id).get());
    }

    public BidDTO findHighestNumberOfBets(Long id) { // Возвращает имя ставившего на данный лот наибольшее количество раз
        return BidDTO.fromBid(bidRepository.findHighestNumberOfBets(id).get());
    }

    public FullLot findAllInformationAboutLot(Long id) { // Получить полную информацию о лоте
        FullLot fullLot = FullLot.fromLot(lotRepository.findById(id).get());
        Integer currentPrice = currentPrice(id, fullLot.getBidPrice(), fullLot.getStartPrice());
        fullLot.setCurrentPrice(currentPrice);
        fullLot.setLastBid(getInfoAboutFirstBidder(id).toBid());
        return fullLot;
    }

    public CreateLot updateToStartedLot(CreateLot createLot) { // Начать торги по лоту
        Lot lot = createLot.toLot();
        lot.setStatus(Status.STARTED.toString());
        Lot createdLot = lotRepository.save(lot);
        logger.info("Created lot: {} ", createLot);
        return CreateLot.fromLot(createdLot);
    }

    //   Сделать ставку по лоту

    public CreateLot updateToStopLot(CreateLot createLot) { // Остановить торги по лоту
        Lot lot = createLot.toLot();
        lot.setStatus(Status.STOPPED.toString());
        Lot createdLot = lotRepository.save(lot);
        logger.info("Created lot: {} ", createLot);
        return CreateLot.fromLot(createdLot);
    }

    public CreateLot createLot(CreateLot createLot) { // Создает новый лот
        Lot lot = createLot.toLot();
        lot.setStatus(Status.CREATED.toString());
        Lot createdLot = lotRepository.save(lot);
        logger.info("Created lot: {} ", createLot);
        return CreateLot.fromLot(createdLot);
    }


    public List<CreateLot> findLotsByStatus(String status, Pageable pageable) { //Получить все лоты, основываясь на фильтре статуса и номере страницы
        logger.info("Found all lots where status: {}", status);
        return lotRepository.findAllByStatusContainingIgnoreCase(status, pageable)
                .stream()
                .map(CreateLot::fromLot)
                .collect(Collectors.toList());
    }

    public BidDTO createBid(BidDTO bidDTO) {
        Bid bid = bidDTO.toBid();
        bid.setBidDate(LocalDateTime.now());
        Bid createdBid = bidRepository.save(bid);
        logger.info("Created bid: {} ", createdBid);
        return BidDTO.fromBid(createdBid);
    }

    public FullLot findLotById(Long id) {
        logger.info("Find lot with id {}", id);
        return FullLot.fromLot(lotRepository.findById(id).get());
    }

    private Integer currentPrice(Long id, Integer bidPrice, Integer startPrice) {
        return Math.toIntExact((bidRepository.bidCount(id) * bidPrice + startPrice));
    }


    //Метод с экспортом в csv напишу как пойму что не тупой


}
