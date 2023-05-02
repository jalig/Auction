package com.skypro.auction.controller;

import com.skypro.auction.dto.BidDTO;
import com.skypro.auction.dto.CreateLot;
import com.skypro.auction.dto.FullLot;
import com.skypro.auction.dto.LotDTO;
import com.skypro.auction.enums.Status;
import com.skypro.auction.projection.BidView;
import com.skypro.auction.service.LotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/lot")
public class Lots {

    private final LotService lotService;

    public Lots(LotService lotService) {
        this.lotService = lotService;
    }

    /**
     * Получить информацию о первом ставившем на лот
     */
    @GetMapping("/{id}/first")
    public ResponseEntity<CreateLot> getInfoAboutFirstBidder(@PathVariable Long id) {
        CreateLot firstBidder = lotService.findLotWithFirstBidder(id);
        return ResponseEntity.ok(firstBidder);
    }

    /**
     * Возвращает имя ставившего на данный лот наибольшее количество раз
     */
    @GetMapping("/{id}/frequent")
    public ResponseEntity<BidView> findHighestNumberOfBets(@PathVariable Long id) {
        BidView bidView = lotService.findHighestNumberOfBets(id);
        return ResponseEntity.ok(bidView);
    }

    /**
     * Получить полную информацию о лоте
     */
    @GetMapping("/{id}")
    public ResponseEntity<FullLot> findAllInformationAboutLot(@PathVariable Long id) {
        return ResponseEntity.ok(lotService.findAllInformationAboutLot(id));
    }

    /**
     * Начать торги по лоту
     */
    @PostMapping("/{id}/start")
    public ResponseEntity<String> startBidding(@PathVariable Long id) {
        lotService.updateStatus(id, Status.STARTED);
        return ResponseEntity.ok("Лот переведен в статус начато");
    }

    /**
     * Остановить торги по лоту
     */
    @PostMapping("/{id}/stop")
    public ResponseEntity<String> stopBidding(@PathVariable Long id) {
        lotService.updateStatus(id, Status.STOPPED);
        return ResponseEntity.ok("Лот переведен в статус остановлен");
    }

    /**
     * Сделать ставку по лоту
     */
    @PostMapping("/{id}/bid")
    public ResponseEntity<BidDTO> placeBet(@PathVariable Long id,
                                           @RequestBody BidDTO bidDTO) {
        lotService.createBidByLotId(id, bidDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Создает новый лот
     */
    @PostMapping
    public ResponseEntity<LotDTO> createLot(@RequestBody CreateLot createLot) {
        LotDTO createdLot = lotService.createLot(createLot);
        return ResponseEntity.ok(createdLot);
    }

    /**
     * Получить все лоты, основываясь на фильтре статуса и номере страницы
     */
    @GetMapping
    public ResponseEntity<Collection<LotDTO>> getAllLots(
            @RequestParam(required = false) Status lotStatus,
            @RequestParam(name = "page", required = false) Integer pageNumber
    ) {
        return ResponseEntity.ok(lotService.findLotsByStatus(lotStatus.toString(), pageNumber));
    }

    /**
     * Экспортировать все лоты в файл CSV
     */
    @GetMapping("/export")
    public void downloadLotTable(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"lots.csv\"");
        lotService.exportLotsToCsv(response.getWriter());
    }

}
