package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import com.codingshuttle.project.uber.UberAppBackend.entities.Wallet;
import com.codingshuttle.project.uber.UberAppBackend.entities.WalletTransaction;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsMethod;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsType;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.WalletRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.WalletService;
import com.codingshuttle.project.uber.UberAppBackend.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImplementation implements WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper modelMapper;
    private final WalletTransactionService walletTransactionService;


    @Override
    @Transactional
    public Wallet addManyToWallet(User user, Double amount,
                                  String transactionId, Ride ride,
                                  TransactionsMethod transactionsMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()+amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionsType(TransactionsType.CREDIT)
                .transactionsMethod(transactionsMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);

    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount,
                                        String transactionId, Ride ride,
                                        TransactionsMethod transactionsMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance()-amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionsType(TransactionsType.DEBIT)
                .transactionsMethod(transactionsMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(
                walletTransaction);
        //        wallet.getTransactions().add(walletTransaction);

        return walletRepository.save(wallet);

    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(()-> new ResourceNotFoundExceptions("WALLET NOT FOUND WITH ID: "+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundExceptions("WALLET NOT FOUND FOR USER WITH ID: " +user.getId()));
    }
}
