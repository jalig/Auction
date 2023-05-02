package com.skypro.auction.exception;

public class IncorrectLotStatusException extends RuntimeException {
    public IncorrectLotStatusException(String message) {
        super(message);
    }
}
