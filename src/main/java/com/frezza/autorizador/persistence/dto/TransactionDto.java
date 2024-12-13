package com.frezza.autorizador.persistence.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    @NotBlank(message = "Número do cartão é obrigatório.")
    private String numeroCartao;
    @NotBlank(message = "Senha do cartão é obrigatória.")
    private String senhaCartao;
    @DecimalMin(value = "0.01", inclusive = true, message = "O valor mínimo da transação deve ser 0.01.")
    private BigDecimal valor;
}
