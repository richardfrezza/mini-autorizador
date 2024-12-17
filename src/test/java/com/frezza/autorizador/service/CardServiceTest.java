package com.frezza.autorizador.service;

import com.frezza.autorizador.exception.CardAlreadyExistsException;
import com.frezza.autorizador.exception.CardNotExistsException;
import com.frezza.autorizador.persistence.dto.CardDto;
import com.frezza.autorizador.persistence.dto.CardResponseDto;
import com.frezza.autorizador.persistence.mapper.CardMapper;
import com.frezza.autorizador.persistence.model.Card;
import com.frezza.autorizador.persistence.repository.CardRepository;
import com.frezza.autorizador.utils.Models;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService service;

    @Mock
    private CardRepository repository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Spy
    private CardMapper mapper;

    private Card card;

    private CardDto cardDto;

    @BeforeEach
    public void setUp() {
        cardDto = Models.createCardDto();
        card = Models.createCard();
        ReflectionTestUtils.setField(service, "balanceInitial", BigDecimal.valueOf(500.00));
    }

    @Test
    void whenCriaCartao_thenSucess() {
        when(repository.findByNumber(cardDto.getNumeroCartao())).thenReturn(null);
        when(passwordEncoder.encode(cardDto.getSenha())).thenReturn("encodedPassword");
        when(repository.save(any(Card.class))).thenReturn(card);

        CardResponseDto result = service.createCard(cardDto);
        assertNotNull(result);
        assertEquals("9999999999999999", result.getNumeroCartao());
        assertEquals("admin", result.getSenha());
        verify(repository, times(1)).findByNumber("9999999999999999");
        verify(repository, times(1)).save(any(Card.class));
    }

    @Test
    void whenCriaCartaoJaCadastrado_thenError() {
        when(repository.findByNumber("9999999999999999")).thenReturn(card);

        CardAlreadyExistsException cardAlreadyExistsException = assertThrows(CardAlreadyExistsException.class,
                () -> service.createCard(cardDto));
        assertEquals(cardDto.getNumeroCartao(), cardAlreadyExistsException.getCardNumber());
        assertEquals(cardDto.getSenha(), cardAlreadyExistsException.getPassword());
    }

    @Test
    void whenConsultaSaldo_thenSucess() {
        Optional<BigDecimal> initValue = Optional.of(new BigDecimal("500.00"));
        when(repository.findBalanceByNumber(any(String.class))).thenReturn(initValue);

        BigDecimal balance = service.getBalance("123456");
        assertNotNull(balance);
        assertEquals(new BigDecimal("500.00"), balance);
    }

    @Test
    void whenConsultaSaldoCartaoInexistente_thenUnprocessable() {
        when(repository.findBalanceByNumber(any(String.class))).thenReturn(Optional.empty());

        CardNotExistsException cardAlreadyExistsException = assertThrows(CardNotExistsException.class,
                () -> service.getBalance("123456"));
        assertEquals("Cartão não encontrado", cardAlreadyExistsException.getMessage());
    }



}

