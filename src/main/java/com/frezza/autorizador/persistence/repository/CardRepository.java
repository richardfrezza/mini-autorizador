package com.frezza.autorizador.persistence.repository;

import com.frezza.autorizador.persistence.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {

   Card findByNumber(String number);

   @Query("SELECT c.balance FROM Card c WHERE c.number = :number")
   Optional<BigDecimal> findBalanceByNumber(@Param("number") String number);

}
