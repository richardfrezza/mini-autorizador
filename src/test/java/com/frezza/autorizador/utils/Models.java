package com.frezza.autorizador.utils;

import com.frezza.autorizador.persistence.dto.CardDto;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;

import java.math.BigDecimal;

public class Models {

    public static CardDto createCardDto() {
        CardDto dto = new CardDto();
        dto.setNumeroCartao("9999999999999999");
        dto.setSenha("admin");
        return dto;
    }

    public static Card createCard() {
        Card card = new Card();
        card.setNumber("9999999999999999");
        card.setPassword("admin");
        card.setBalance(new BigDecimal("500.00"));
        return card;
    }

    public static TransactionDto createTransactionDto() {
        TransactionDto dto = new TransactionDto();
        dto.setNumeroCartao("9999999999999999");
        dto.setValor(BigDecimal.valueOf(10));
        dto.setSenhaCartao("admin");
        return dto;
    }

}
