package com.codingshuttle.project.uber.UberAppBackend.services;

import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import com.codingshuttle.project.uber.UberAppBackend.entities.Wallet;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsMethod;

public interface WalletService {

    Wallet  addManyToWallet(User user, Double amount,
                            String transactionId, Ride ride,
                            TransactionsMethod transactionsMethod);

    Wallet deductMoneyFromWallet(User user, Double amount,
                                 String transactionId, Ride ride,
                                 TransactionsMethod transactionsMethod);;

    void withdrawAllMyMoneyFromWallet();
    Wallet findWalletById(Long walletId);
    Wallet createNewWallet(User user);
    Wallet findByUser(User user);

}
