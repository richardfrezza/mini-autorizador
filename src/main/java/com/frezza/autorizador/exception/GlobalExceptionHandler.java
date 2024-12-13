package com.frezza.autorizador.exception;

import com.frezza.autorizador.persistence.dto.CardDto;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return new ResponseEntity<>("Erro interno do servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CardNotExistsException.class)
    public ResponseEntity<Object> handleCardNotExistsException(CardNotExistsException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CardAlreadyExistsException.class)
    public ResponseEntity<CardDto> handleCardAlreadyExistsException(CardAlreadyExistsException e) {
        CardDto cardDto = new CardDto();
        cardDto.setSenha("******");
        cardDto.setNumeroCartao(e.getMessage());
        return ResponseEntity.unprocessableEntity().body(cardDto);
    }

    @ExceptionHandler({TransactionException.class})
    public ResponseEntity<String> handleTransactionException(TransactionException e) {
        return ResponseEntity.unprocessableEntity().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({StaleObjectStateException.class})
    public ResponseEntity<String> handleStaleObjectStateException(StaleObjectStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocorreu um falha na transação, tente mais tarde");
    }

}
