package com.frezza.autorizador.exception;

import com.frezza.autorizador.persistence.dto.CardDto;
import lombok.Getter;

@Getter
public class CardAlreadyExistsException extends RuntimeException {

    private final String password;
    private final String cardNumber;

    public CardAlreadyExistsException(CardDto cardDto) {
        super("Cartão já cadastrado !");
        this.password = cardDto.getSenha();
        this.cardNumber = cardDto.getNumeroCartao();
    }

}
