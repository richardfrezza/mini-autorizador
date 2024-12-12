package com.frezza.autorizador.exception;

import lombok.Getter;

@Getter
public class CardAlreadyExistsException extends RuntimeException {

    public CardAlreadyExistsException(String message) {
        super(message);
    }

}
