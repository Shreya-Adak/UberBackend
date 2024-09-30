package com.codingshuttle.project.uber.UberAppBackend.services.implementations;


import com.codingshuttle.project.uber.UberAppBackend.entities.WalletTransaction;
import com.codingshuttle.project.uber.UberAppBackend.repositories.WalletTransactionRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImplementation implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;


    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);



    }
}
