package com.skypro.auction.exception.handlers;

import com.skypro.auction.exception.BadLotCreationException;
import com.skypro.auction.exception.IncorrectLotStatusException;
import com.skypro.auction.exception.LotNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundControllerAdvice {

    @ExceptionHandler(LotNotFoundException.class)
    public ResponseEntity<?> notFound() {
        return ResponseEntity.status(404).body("Лот не найден");
    }

    @ExceptionHandler(IncorrectLotStatusException.class)
    public ResponseEntity<?> badStatus() {
        return ResponseEntity.status(400).body("Лот в неверном статусе");
    }

    @ExceptionHandler(BadLotCreationException.class)
    public ResponseEntity<?> badLot() {
        return ResponseEntity.status(400).body("Лот передан с ошибкой");
    }
}
