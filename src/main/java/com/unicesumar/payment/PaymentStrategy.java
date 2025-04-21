package com.unicesumar.payment;

public interface PaymentStrategy {
    boolean processPayment(double amount);
    String getTransactionDetails();
} 