package com.skypro.auction.controller;

import com.skypro.auction.dto.BidDTO;
import com.skypro.auction.dto.CreateLot;
import com.skypro.auction.dto.FullLot;
import com.skypro.auction.enums.Status;
import com.skypro.auction.service.LotService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/lot")
public class Lots {


    private final LotService lotService;

    public Lots(LotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping("/{id}/first") //Получить информацию о первом ставившем на лоте
    public ResponseEntity<BidDTO> getInfoAboutFirstBidder(@PathVariable Long id) {
        if (lotService.findLotById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (lotService.findLotById(id).getStatus().equals(Status.CREATED.toString())) {
            return ResponseEntity.badRequest().build();
        }
        BidDTO firstBidder = lotService.getInfoAboutFirstBidder(id);
        return ResponseEntity.ok(firstBidder);
    }

    @GetMapping("/{id}/frequent") //Возвращает имя ставившего на данный лот наибольшее количество раз
    public ResponseEntity<BidDTO> findHighestNumberOfBets(@PathVariable Long id) {
        if (lotService.findLotById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        if (lotService.findLotById(id).getStatus().equals(Status.CREATED.toString())) {
            return ResponseEntity.badRequest().build();
        }
        BidDTO bidDTO = lotService.findHighestNumberOfBets(id);
        return ResponseEntity.ok(bidDTO);
    }

    @GetMapping("/{id}") //Получить полную информацию о лоте
    public ResponseEntity<FullLot> findAllInformationAboutLot(@PathVariable Long id) {
        if (lotService.findLotById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lotService.findAllInformationAboutLot(id));
    }

    @PostMapping("/{id}/start") //Начать торги по лоту
    public ResponseEntity<FullLot> startBidding(@PathVariable Long id) {
        FullLot updatedLot = lotService.findLotById(id);
        if (updatedLot == null) {
            return ResponseEntity.notFound().build();
        }
        if (updatedLot.getStatus().equals(Status.STARTED.toString())) {
            return ResponseEntity.ok().build();
        }
        if (updatedLot.getStatus().equals(Status.STOPPED.toString())) {
            return ResponseEntity.badRequest().build();
        }
        if (updatedLot.getStatus().equals(Status.CREATED.toString())) {
            lotService.updateToStartedLot(id);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bid") //Сделать ставку по лоту
    public ResponseEntity<BidDTO> placeBet(@PathVariable Long id,
                                           @RequestBody BidDTO bidDTO) {
        FullLot biddingLot = lotService.findLotById(id);
        if (biddingLot == null) {
            return ResponseEntity.notFound().build();
        }
        if (biddingLot.getStatus().equals(Status.CREATED.toString())
                || biddingLot.getStatus().equals(Status.STOPPED.toString())) {
            return ResponseEntity.badRequest().build();
        }
        bidDTO.setLotId(id);
        lotService.createBid(bidDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/stop") //Остановить торги по лоту
    public ResponseEntity<FullLot> stopBidding(@PathVariable Long id) {
        FullLot updatedLot = lotService.findLotById(id);
        if (updatedLot == null) {
            return ResponseEntity.notFound().build();
        }
        if (updatedLot.getStatus().equals(Status.STOPPED.toString())) {
            return ResponseEntity.ok().build();
        }
        if (updatedLot.getStatus().equals(Status.CREATED.toString())) {
            return ResponseEntity.badRequest().build();
        }
        if (updatedLot.getStatus().equals(Status.STARTED.toString())) {
            lotService.updateToStoppedLot(id);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping //Создает новый лот
    public ResponseEntity<CreateLot> createLot(@RequestBody CreateLot createLot) {
        CreateLot createdLot = lotService.createLot(createLot);
        return ResponseEntity.ok(createdLot);
    }


    @GetMapping  //Получить все лоты, основываясь на фильтре статуса и номере страницы
    @Parameters({
            @Parameter(name = "page"),
            @Parameter(name = "size"),
    })
    public ResponseEntity<Collection<FullLot>> getAllLots(
            @Parameter(hidden = true) @PageableDefault(size = 10) Pageable pageable,
            @RequestParam Status lotStatus
    ) {

        return ResponseEntity.ok(lotService.findLotsByStatus(lotStatus.toString(), pageable));
    }

//    @GetMapping("/export") //Экспортировать все лоты в файл CSV
//    public void downloadLotTable(HttpServletResponse response) throws IOException {
//        Collection<FullLot> lots = lotService.getAllLotsForExport();
//        StringWriter writer = new StringWriter();
//        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
//
//        for (FullLot lot : lots) {
//            csvPrinter.printRecord(lot.getId(),
//                    lot.getTitle(),
//                    lot.getStatus(),
//                    lot.getLastBid() != null ? lot.getLastBid().getBidderName() : "",
//                    lot.getCurrentPrice());
//        }
//        csvPrinter.flush();
//
//        response.setContentType("text/csv");
//        response.setHeader("Content-Disposition", "attachment; filename=\"lots.csv\"");
//
//        PrintWriter pWriter = response.getWriter();
//        pWriter.write(writer.toString());
//        pWriter.flush();
//        pWriter.close();
//    }

}
