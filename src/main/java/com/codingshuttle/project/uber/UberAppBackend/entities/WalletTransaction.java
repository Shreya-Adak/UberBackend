package com.codingshuttle.project.uber.UberAppBackend.entities;

import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsMethod;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_wallet_transaction_wallet",columnList = "wallet_id"),
        @Index(name = "idx_wallet_transaction_ride",columnList = "ride_id")
})
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private TransactionsType transactionsType;

    private TransactionsMethod transactionsMethod;

    @ManyToOne
    private Ride ride;

    private String transactionId;

    @ManyToOne
    private Wallet wallet;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
