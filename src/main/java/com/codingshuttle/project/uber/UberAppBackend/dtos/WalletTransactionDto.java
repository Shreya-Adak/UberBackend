package com.codingshuttle.project.uber.UberAppBackend.dtos;

import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsMethod;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionsType transactionsType;

    private TransactionsMethod transactionsMethod;


    private RideDto ride;

    private String transactionId;


    private WalletDto wallet;


    private LocalDateTime timestamp;

}
