package com.frezza.autorizador.exception;

import lombok.Getter;

@Getter
public class CardBalanceInsufficientException extends RuntimeException {

    public CardBalanceInsufficientException(String message) {
        super(message);
    }

}
