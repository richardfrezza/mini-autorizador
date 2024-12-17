package com.frezza.autorizador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frezza.autorizador.enums.ApiResponseEnum;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.persistence.model.Card;
import com.frezza.autorizador.persistence.repository.CardRepository;
import com.frezza.autorizador.utils.AutorizadorIntegrationTestConfig;
import com.frezza.autorizador.utils.Models;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Base64;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutorizadorIntegrationTestConfig
public class TransactionControlllerTest {

    private static final String URL = "/transacoes";

    public static final String BASIC_AUTH = "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CardRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    private TransactionDto transactionDto;

    private Card card;

    @BeforeEach
    public void setUp() {
        transactionDto = Models.createTransactionDto();
        card = Models.createCard();
    }

    @Test
    @Order(1)
    void whenRealizandoPrimeiraTransacao_thenSucess() throws Exception {
        card.setPassword(passwordEncoder.encode(card.getPassword()));
        repository.save(card);
        String json = mapper.writeValueAsString(transactionDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(ApiResponseEnum.OK.name()));
    }

    @Test
    void whenTransacionandoComCartaoInexistente_thenUnprocessable() throws Exception {
        transactionDto.setNumeroCartao("00000000000000");
        String json = mapper.writeValueAsString(transactionDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").value(ApiResponseEnum.CARTAO_INEXISTENTE.name()));
    }

    @Test
    void whenTransacionandoComSaldoInsuficiente_thenUnprocessable() throws Exception {
        transactionDto.setValor(BigDecimal.valueOf(550));
        String json = mapper.writeValueAsString(transactionDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").value(ApiResponseEnum.SALDO_INSUFICIENTE.name()));
    }

    @Test
    void whenTransacionandoComSenhaInvalida_thenUnprocessable() throws Exception {
        transactionDto.setSenhaCartao("senha_invalida");
        String json = mapper.writeValueAsString(transactionDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$").value(ApiResponseEnum.SENHA_INVALIDA.name()));
    }

    @Test
    void whenTransacionandoSemSenha_thenBadRequest() throws Exception {
        transactionDto.setSenhaCartao(null);

        String json = mapper.writeValueAsString(transactionDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenTransacionandoCartaoSemNumero_thenBadRequest() throws Exception {
        transactionDto.setNumeroCartao(null);

        String json = mapper.writeValueAsString(transactionDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCriandoCartaoSemLogin_thenUnauthorized() throws Exception {
        MockHttpServletRequestBuilder builder  = MockMvcRequestBuilders
                .post(URL)
                .header("Authorization", "Basic 123:123");

        mockMvc.perform(builder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenCriandoDuasTransacoesSimultaneas_thenOptimist() {
        String numberCard = "1111111111111111";
        card.setNumber(numberCard);
        repository.save(card);

        Card transactionalCard = repository.findByNumber(numberCard);
        Card transactionalCard2 = repository.findByNumber(numberCard);

        transactionalCard.setBalance(BigDecimal.TEN);
        transactionalCard2.setBalance(BigDecimal.TEN);

        repository.save(transactionalCard);
        Assertions.assertThrows(ObjectOptimisticLockingFailureException.class, () -> repository.save(transactionalCard2));

    }

    private MockHttpServletRequestBuilder createPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(URL)
                .header("Authorization", BASIC_AUTH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createGetRequest(String path) {
        return MockMvcRequestBuilders
                .get(URL + "/" + path)
                .header("Authorization", BASIC_AUTH)
                .contentType(MediaType.APPLICATION_JSON);
    }

}
