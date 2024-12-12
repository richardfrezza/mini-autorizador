package com.frezza.autorizador.exception;

import lombok.Getter;

@Getter
public class CardException extends RuntimeException {

    public CardException(String message) {
        super(message);
    }

}
