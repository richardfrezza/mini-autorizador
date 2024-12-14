package com.frezza.autorizador.persistence.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CardResponseDto {
    private String numeroCartao;
    private String senha;
}
