package com.frezza.autorizador.exception;

import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }

}
