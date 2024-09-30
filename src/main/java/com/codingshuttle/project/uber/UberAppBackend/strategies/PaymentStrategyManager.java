package com.codingshuttle.project.uber.UberAppBackend.strategies;


import com.codingshuttle.project.uber.UberAppBackend.entities.enums.PaymentMethod;
import com.codingshuttle.project.uber.UberAppBackend.strategies.implementations.CashPaymentStrategyImplementation;
import com.codingshuttle.project.uber.UberAppBackend.strategies.implementations.WalletPaymentStrategyImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategyImplementation walletPaymentStrategyImplementation;
    private final CashPaymentStrategyImplementation cashPaymentStrategyImplementation;


    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethods){
        return switch(paymentMethods){
            case WALLET -> walletPaymentStrategyImplementation;
            case CASH -> cashPaymentStrategyImplementation;
            //default -> throw new RuntimeException("INVALID PAYMENT METHOd");
        };

    }




}
