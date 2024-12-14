package com.frezza.autorizador.controller;

import com.frezza.autorizador.enums.ApiResponseEnum;
import com.frezza.autorizador.persistence.dto.TransactionDto;
import com.frezza.autorizador.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("transacoes")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> handleTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        transactionService.handleTransaction(transactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseEnum.OK.name());
    }
}
