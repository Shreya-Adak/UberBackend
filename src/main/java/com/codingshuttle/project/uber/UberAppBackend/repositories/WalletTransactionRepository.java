package com.codingshuttle.project.uber.UberAppBackend.repositories;

import com.codingshuttle.project.uber.UberAppBackend.entities.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {
}
