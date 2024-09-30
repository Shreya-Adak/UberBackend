package com.codingshuttle.project.uber.UberAppBackend.services.implementations;

import com.codingshuttle.project.uber.UberAppBackend.entities.Payment;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.enums.PaymentStatus;
import com.codingshuttle.project.uber.UberAppBackend.exceptions.ResourceNotFoundExceptions;
import com.codingshuttle.project.uber.UberAppBackend.repositories.PaymentRepository;
import com.codingshuttle.project.uber.UberAppBackend.services.PaymentService;
import com.codingshuttle.project.uber.UberAppBackend.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {

        Payment payment = paymentRepository.findByRide(ride)
                        .orElseThrow(()-> new ResourceNotFoundExceptions("PAYMENT NOT FOUND FOR RIDE WITH ID: "+ride.getId()));
        paymentStrategyManager.paymentStrategy(
                payment.getPaymentMethods()).processPayment(payment);
    }

    @Override
    public Payment createPayment(Ride ride) {

        Payment payment = Payment.builder()
                .ride(ride)
                .paymentMethods(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
      payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }
}
