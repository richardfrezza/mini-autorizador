package com.frezza.autorizador.service.validator;

import com.frezza.autorizador.enums.ApiResponseEnum;
import com.frezza.autorizador.exception.TransactionException;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class PasswordValidatorChain implements TransactionValidatorChain {

    private final TransactionValidatorChain nextValidator;

    @Override
    public void validate(TransactionDto transactionDto, Card card) throws TransactionException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(transactionDto.getSenhaCartao(), card.getPassword())) {
            throw new TransactionException(ApiResponseEnum.SENHA_INVALIDA.name());
        }
        Optional.ofNullable(nextValidator).ifPresent(e -> e.validate(transactionDto, card));
    }
}
