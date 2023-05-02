package com.skypro.auction.exception;

public class LotNotFoundException extends RuntimeException {
    public LotNotFoundException(String message) {
        super(message);
    }
}
