package com.frezza.autorizador.service;

import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;
import com.frezza.autorizador.persistence.repository.CardRepository;
import com.frezza.autorizador.service.validator.BalanceValidatorChain;
import com.frezza.autorizador.service.validator.CardExistsValidatorChain;
import com.frezza.autorizador.service.validator.PasswordValidatorChain;
import com.frezza.autorizador.service.validator.TransactionValidatorChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final CardRepository cardRepository;

    @Transactional
    @Retryable(retryFor = StaleObjectStateException.class, backoff = @Backoff(delay = 200))
    public void handleTransaction(TransactionDto transactionDto) {
        Card card = getCardByNumber(transactionDto.getNumeroCartao());
        validateTransactionChain(transactionDto, card);
        decreaseBalance(transactionDto, card);
        cardRepository.save(card);
    }

    private Card getCardByNumber(String numberCard) {
        return cardRepository.findByNumber(numberCard);
    }

    private void validateTransactionChain(TransactionDto transactionDto, Card card) {
        TransactionValidatorChain transactionValidatorChain =
            new CardExistsValidatorChain(
            new PasswordValidatorChain(
            new BalanceValidatorChain(null))
        );
        transactionValidatorChain.validate(transactionDto, card);
    }

    private void decreaseBalance(TransactionDto transactionDto, Card card) {
        card.setBalance(card.getBalance().subtract(transactionDto.getValor()));
    }
}
