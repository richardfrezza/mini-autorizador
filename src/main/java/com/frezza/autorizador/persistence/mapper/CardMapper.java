package com.frezza.autorizador.persistence.mapper;

import com.frezza.autorizador.persistence.dto.CardDto;
import com.frezza.autorizador.persistence.dto.CardResponseDto;
import org.springframework.stereotype.Component;

// Para aplicações maiores usaria MapStruct
@Component
public class CardMapper {

    public CardResponseDto cardDtoToCardResponse(CardDto cardDto) {
       return CardResponseDto.builder()
           .numeroCartao(cardDto.getNumeroCartao())
           .senha(cardDto.getSenha())
           .build();
    }

}
