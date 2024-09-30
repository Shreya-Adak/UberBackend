package com.codingshuttle.project.uber.UberAppBackend.strategies;


import com.codingshuttle.project.uber.UberAppBackend.entities.Payment;

public interface PaymentStrategy {


   static final Double PLATFORM_COMMISSION = 0.3;
   void processPayment(Payment payment);


}
