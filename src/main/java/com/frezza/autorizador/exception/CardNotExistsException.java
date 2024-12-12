package com.frezza.autorizador.exception;

import lombok.Getter;

@Getter
public class CardNotExistsException extends RuntimeException {

    public CardNotExistsException(String message) {
        super(message);
    }

}
