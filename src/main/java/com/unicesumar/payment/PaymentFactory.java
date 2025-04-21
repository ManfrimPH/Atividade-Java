package com.unicesumar.payment;

public class PaymentFactory {
    public static PaymentStrategy createPayment(int option) {
        switch (option) {
            case 1:
                return new CreditCardPayment();
            case 2:
                return new BoletoPayment();
            case 3:
                return new PixPayment();
            default:
                throw new IllegalArgumentException("Opção de pagamento inválida");
        }
    }
} 