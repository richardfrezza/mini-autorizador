package com.frezza.autorizador.controller;

import com.frezza.autorizador.persistence.dto.CardDto;
import com.frezza.autorizador.persistence.dto.CardResponseDto;
import com.frezza.autorizador.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("cartoes")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CardDto cardDto) {
        return new ResponseEntity<>(cardService.createCard(cardDto), HttpStatus.CREATED);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable("numeroCartao") String numeroCartao) {
        return new ResponseEntity<>(cardService.getBalance(numeroCartao), HttpStatus.OK);
    }

}
