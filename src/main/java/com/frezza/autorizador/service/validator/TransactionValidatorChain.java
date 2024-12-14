package com.frezza.autorizador.service.validator;

import com.frezza.autorizador.exception.TransactionException;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;

public interface TransactionValidatorChain {
    void validate(TransactionDto transactionDto, Card card) throws TransactionException;
}
