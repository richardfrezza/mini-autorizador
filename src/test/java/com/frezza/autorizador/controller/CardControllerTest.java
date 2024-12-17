package com.frezza.autorizador.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frezza.autorizador.persistence.dto.CardDto;
import com.frezza.autorizador.persistence.model.Card;
import com.frezza.autorizador.utils.AutorizadorIntegrationTestConfig;
import com.frezza.autorizador.utils.Models;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutorizadorIntegrationTestConfig
 class CardControllerTest {

    private static final String URL = "/cartoes";

    public static final String BASIC_AUTH = "Basic " + Base64.getEncoder().encodeToString("username:password".getBytes());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private CardDto cardDto;

    private Card card;

    @BeforeEach
    public void setUp() {
        cardDto = Models.createCardDto();
        card = Models.createCard();
    }

    @Test
    @Order(0)
    void whenCriandoCard_thenSucess() throws Exception {
        String json = mapper.writeValueAsString(cardDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").isNotEmpty())
                .andExpect(jsonPath("$.senha").isNotEmpty())
                .andExpect(jsonPath("$.numeroCartao").value(cardDto.getNumeroCartao()))
                .andExpect(jsonPath("$.senha").value(cardDto.getSenha()));
    }

    @Test
    @Order(1)
    void whenCartaoJaCadastrado_thenUnprocessable() throws Exception {
        String json = mapper.writeValueAsString(cardDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.numeroCartao").isNotEmpty())
                .andExpect(jsonPath("$.senha").isNotEmpty())
                .andExpect(jsonPath("$.numeroCartao").value(cardDto.getNumeroCartao()))
                .andExpect(jsonPath("$.senha").value(cardDto.getSenha()));
    }

    @Test
    @Order(3)
    void whenBuscandoSaldoCartaoCadastro_thenOkSaldoIgual500() throws Exception {
        MvcResult result = mockMvc.perform(createGetRequest(cardDto.getNumeroCartao()))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.DOWN), new BigDecimal(result.getResponse().getContentAsString()));
    }

    @Test
    void whenBuscandoSaldoCartaoInexistente_thenNotFoundNoBody() throws Exception {
        mockMvc.perform(createGetRequest("8888888888888888"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void whenCriandoCartaoSemSenha_thenBadRequest() throws Exception {
        cardDto.setSenha(null);
        
        String json = mapper.writeValueAsString(cardDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCriandoCartaoSemNumero_thenBadRequest() throws Exception {
        cardDto.setNumeroCartao(null);
        
        String json = mapper.writeValueAsString(cardDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCriandoCartaoComNumeroInvalido_thenBadRequest() throws Exception {
        cardDto.setNumeroCartao("ABCDFG");

        String json = mapper.writeValueAsString(cardDto);
        mockMvc.perform(createPostRequest(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCriandoCartaoComNumuroMaiorQueLimiteCaracteres_thenBadRequest() throws Exception {
        cardDto.setNumeroCartao("77777777777777777777");
        String json = mapper.writeValueAsString(cardDto);

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

    private MockHttpServletRequestBuilder createPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(URL)
                .header("Authorization", BASIC_AUTH)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createGetRequest(String path) {
        return MockMvcRequestBuilders
                .get(URL + "/" + path)
                .header("Authorization", BASIC_AUTH)
                .contentType(MediaType.APPLICATION_JSON);
    }

}
