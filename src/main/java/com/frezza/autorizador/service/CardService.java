package com.frezza.autorizador.service;

import com.frezza.autorizador.exception.CardAlreadyExistsException;
import com.frezza.autorizador.exception.CardNotExistsException;
import com.frezza.autorizador.persistence.dto.CardDto;
import com.frezza.autorizador.persistence.dto.CardResponseDto;
import com.frezza.autorizador.persistence.mapper.CardMapper;
import com.frezza.autorizador.persistence.model.Card;
import com.frezza.autorizador.persistence.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CardMapper mapper;

    @Value("${card.balance.initial}")
    private BigDecimal balanceInitial;

    @Transactional
    public CardResponseDto createCard(CardDto cardDto) {
        validateCard(cardDto);
        Card card = buildCard(cardDto);
        cardRepository.save(card);
        return mapper.cardDtoToCardResponse(cardDto);
    }

    public BigDecimal getBalance(String numeroCartao) {
        return cardRepository.findBalanceByNumber(numeroCartao)
           .orElseThrow(() -> new CardNotExistsException("Cartão não encontrado"));
    }

    private void validateCard(CardDto cardDto) {
        Optional.ofNullable(cardRepository.findByNumber(cardDto.getNumeroCartao())).ifPresent(card -> {
            throw new CardAlreadyExistsException(cardDto);
        });
    }

    private Card buildCard(CardDto cardDto) {
        Card card = new Card();
        card.setBalance(balanceInitial.setScale(2, RoundingMode.DOWN));
        card.setNumber(cardDto.getNumeroCartao());
        card.setPassword(passwordEncoder.encode(cardDto.getSenha()));
        return card;
    }
}
