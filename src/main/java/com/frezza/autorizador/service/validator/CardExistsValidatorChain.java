package com.frezza.autorizador.service.validator;

import com.frezza.autorizador.enums.ApiResponseEnum;
import com.frezza.autorizador.exception.TransactionException;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CardExistsValidatorChain implements TransactionValidatorChain {

    private final TransactionValidatorChain nextValidator;

    @Override
    public void validate(TransactionDto transactionDto, Card card) throws TransactionException {
        if (card == null) {
            throw new TransactionException(ApiResponseEnum.CARTAO_INEXISTENTE.name());
        }
        Optional.ofNullable(nextValidator).ifPresent(e -> e.validate(transactionDto, card));
    }
}
