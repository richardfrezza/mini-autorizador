package com.frezza.autorizador.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ApiResponseEnum {
    SALDO_INSUFICIENTE("Saldo insuficiente"),
    SENHA_INVALIDA("Senha inválida"),
    CARTAO_INEXISTENTE("Cartão inexistente"),
    OK("Operação bem-sucedida");

    private final String message;
}
