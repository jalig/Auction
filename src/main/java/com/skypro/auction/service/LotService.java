package com.skypro.auction.service;

import com.skypro.auction.dto.BidDTO;
import com.skypro.auction.dto.CreateLot;
import com.skypro.auction.dto.FullLot;
import com.skypro.auction.dto.LotDTO;
import com.skypro.auction.enums.Status;
import com.skypro.auction.exception.BadLotCreationException;
import com.skypro.auction.exception.IncorrectLotStatusException;
import com.skypro.auction.exception.LotNotFoundException;
import com.skypro.auction.model.Bid;
import com.skypro.auction.model.Lot;
import com.skypro.auction.projection.BidView;
import com.skypro.auction.repository.BidRepository;
import com.skypro.auction.repository.LotRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    public BidView findHighestNumberOfBets(Long id) {
        Optional<BidView> highestNumberOfBets = bidRepository.findHighestNumberOfBets(id);
        if (highestNumberOfBets.isEmpty()) throw new LotNotFoundException("Лот не найден");

        return highestNumberOfBets.get();
    }

    public FullLot findAllInformationAboutLot(Long id) {
        Lot lotById = findLotById(id);
        FullLot fullLot = FullLot.fromLot(lotById);
        Integer currentPrice = currentPrice(id, fullLot.getBidPrice(), fullLot.getStartPrice());
        fullLot.setCurrentPrice(currentPrice);
        fullLot.setLastBid(bidRepository.getInfoAboutLastBidDate(id));
        return fullLot;
    }

    public void updateStatus(Long id, Status status) {
        logger.info("Lot status updated to: {}", status);
        Lot lotById = findLotById(id);
        lotById.setStatus(status);
        lotRepository.save(lotById);
    }

    public LotDTO createLot(CreateLot createLot) {
        validateLot(createLot);
        Lot lot = createLot.toLot();
        lot.setStatus(Status.CREATED);
        lotRepository.save(lot);
        logger.info("Created lot: {} ", createLot);
        return LotDTO.fromLot(lot);
    }

    private void validateLot(CreateLot createLot) {
        if (createLot.getTitle().isEmpty()) throw new BadLotCreationException("Название лота не может быть пустым");
    }


    public List<LotDTO> findLotsByStatus(String status, Integer pageNumber) {
        logger.info("Found all lots where status: {}", status);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        return lotRepository.findAllByStatus(status, pageRequest)
                .getContent()
                .stream()
                .map(LotDTO::fromLot)
                .collect(Collectors.toList());
    }

    private Integer currentPrice(Long id, Integer bidPrice, Integer startPrice) {
        return Math.toIntExact((bidRepository.bidCount(id) * bidPrice + startPrice));
    }

    public Collection<FullLot> getAllLotsForExport() {
        return lotRepository.findAll().stream()
                .map(FullLot::fromLot)
                .peek(lot -> lot.setCurrentPrice(currentPrice(lot.getId(), lot.getBidPrice(), lot.getStartPrice())))
                .peek(lot -> lot.setLastBid(bidRepository.getInfoAboutLastBidDate(lot.getId())))
                .collect(Collectors.toList());
    }


    public CreateLot findLotWithFirstBidder(Long id) {
        Lot lotById = findLotById(id);
        return CreateLot.fromLot(lotById);
    }

    private Lot findLotById(Long id) {
        Optional<Lot> lotOptional = lotRepository.findById(id);

        if (lotOptional.isEmpty()) throw new LotNotFoundException("Lot with id " + id + " not found");

        return lotOptional.get();
    }

    public void createBidByLotId(Long id, BidDTO bidDTO) {
        Lot lotById = findLotById(id);
        if (!lotById.getStatus().equals(Status.STARTED)) {
            throw new IncorrectLotStatusException("IncorrectStatus " + lotById.getStatus());
        }

        Bid bid = bidDTO.toBid();
        bid.setLot(lotById);
        bidRepository.save(bid);
    }

    public void exportLotsToCsv(PrintWriter pWriter) throws IOException {
        Collection<FullLot> lots = getAllLotsForExport();
        CSVPrinter csvPrinter = new CSVPrinter(pWriter, CSVFormat.DEFAULT);

        for (FullLot lot : lots) {
            csvPrinter.printRecord(
                    lot.getId(),
                    lot.getTitle(),
                    lot.getStatus(),
                    lot.getLastBid() != null ? lot.getLastBid().getBidderName() : "",
                    lot.getCurrentPrice());
        }
        csvPrinter.flush();
    }
}
