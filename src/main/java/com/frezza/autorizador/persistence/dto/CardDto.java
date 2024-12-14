package com.frezza.autorizador.persistence.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {
    @NotBlank(message = "Número do cartão é obrigatório.")
    @Digits(integer = 16, fraction = 0, message = "Número do cartão deve conter apenas números com até 16 dígitos.")
    private String numeroCartao;
    @NotBlank(message = "Senha do cartão é obrigatória.")
    private String senha;
}
