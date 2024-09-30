package com.codingshuttle.project.uber.UberAppBackend.services;

import com.codingshuttle.project.uber.UberAppBackend.entities.WalletTransaction;

public interface WalletTransactionService {

    void createNewWalletTransaction(WalletTransaction walletTransaction);

}