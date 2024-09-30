package com.codingshuttle.project.uber.UberAppBackend.strategies.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.Payment;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.PaymentStatus;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsMethod;
import com.codingshuttle.project.uber.UberAppBackend.repositories.PaymentRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.WalletService;
import com.codingshuttle.project.uber.UberAppBackend.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//Rider -> 100
//Driver -> 70 Deduct 30Rs. from Driver's wallet

@Service
@RequiredArgsConstructor
public class CashPaymentStrategyImplementation implements PaymentStrategy {

    private final WalletService walletService;
    //private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {

        Driver driver = payment.getRide().getDriver();
       // Wallet driverWallet = walletService.findByUser(driver.getUser());

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(),
                platformCommission, null,
                payment.getRide(), TransactionsMethod.RIDE);

//paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);

        paymentRepository.save(payment);

    }
}
