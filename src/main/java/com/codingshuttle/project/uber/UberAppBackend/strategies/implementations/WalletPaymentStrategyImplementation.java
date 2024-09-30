package com.codingshuttle.project.uber.UberAppBackend.strategies.implementations;


import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.Payment;
import com.codingshuttle.project.uber.UberAppBackend.entities.Rider;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.PaymentStatus;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.TransactionsMethod;
import com.codingshuttle.project.uber.UberAppBackend.repositories.PaymentRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.WalletService;
import com.codingshuttle.project.uber.UberAppBackend.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


//Rider -> 232
//Ride -> 100
//Rider -> 232 - 100 = 132
//Driver -> 500+(100-30) = 570
@Service
@RequiredArgsConstructor
public class WalletPaymentStrategyImplementation implements PaymentStrategy
{
    private final WalletService walletService;
    //private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null,
                payment.getRide(),TransactionsMethod.RIDE);
        double driversCut = payment.getAmount() * (1-PLATFORM_COMMISSION);
        walletService.addManyToWallet(driver.getUser(), driversCut,
                null,payment.getRide(),
                TransactionsMethod.RIDE);
       // paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);

        paymentRepository.save(payment);

    }
}
