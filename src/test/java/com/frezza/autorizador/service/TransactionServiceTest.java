package com.frezza.autorizador.service;

import com.frezza.autorizador.enums.ApiResponseEnum;
import com.frezza.autorizador.exception.TransactionException;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;
import com.frezza.autorizador.persistence.repository.CardRepository;
import com.frezza.autorizador.utils.Models;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private CardRepository cardRepository;

    private TransactionDto transactionDto;

    private Card card;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        transactionDto = Models.createTransactionDto();
        card = Models.createCard();
        card.setPassword(passwordEncoder.encode(card.getPassword()));
    }

    @Test
    void whenEfetuaTransacacaoDebitando10Reais_thenSuccess() {
        when(cardRepository.findByNumber(transactionDto.getNumeroCartao())).thenReturn(card);

        transactionService.handleTransaction(transactionDto);

        assertEquals(new BigDecimal("490.00").setScale(2, RoundingMode.DOWN), card.getBalance());
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void whenEfetuaTransacaoSemCartaoCadastrado_thenCardNotFound() {
        when(cardRepository.findByNumber(transactionDto.getNumeroCartao())).thenReturn(null);

        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.handleTransaction(transactionDto));
        assertEquals(ApiResponseEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void whenEfetuaTransacaoComSenhaInvalida_thenInvalidPassword() {
        transactionDto.setSenhaCartao("senha_invalida");

        when(cardRepository.findByNumber(transactionDto.getNumeroCartao())).thenReturn(card);

        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.handleTransaction(transactionDto));
        assertEquals(ApiResponseEnum.SENHA_INVALIDA.name(), exception.getMessage());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void whenEfetuaTransacaoSaldoInsuficiente_thenInsufficientBalance() {
        transactionDto.setValor(new BigDecimal("600"));

        when(cardRepository.findByNumber(transactionDto.getNumeroCartao())).thenReturn(card);

        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.handleTransaction(transactionDto));
        assertEquals(ApiResponseEnum.SALDO_INSUFICIENTE.name(), exception.getMessage());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void whenEfetuaTransacaoComErroInesperado_thenUnexpectedException() {
        when(cardRepository.findByNumber(transactionDto.getNumeroCartao())).thenThrow(RuntimeException.class);

        TransactionException exception = assertThrows(TransactionException.class, () -> transactionService.handleTransaction(transactionDto));
        assertNotNull(exception.getMessage());
        assertTrue(exception.getMessage().startsWith("Erro inesperado ao realizar a transação"));
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void whenEfetuaTransacaoComErroDeConcorrenciaOtimista_thenStaleObjectStateException() {
        TransactionService spyService = spy(transactionService);

        doThrow(new StaleObjectStateException("old version", card)).when(spyService).handleTransaction(transactionDto);

        assertThrows(StaleObjectStateException.class, () -> spyService.handleTransaction(transactionDto));
        verify(cardRepository, never()).save(any(Card.class));
    }

}

