package com.frezza.autorizador.persistence.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {
    @NotBlank(message = "Número do cartão é obrigatório.")
    private String numeroCartao;
    @NotBlank(message = "Senha do cartão é obrigatória.")
    private String senha;
}
