package com.unicesumar.payment;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        // Simulação de processamento de pagamento com cartão
        System.out.println("Processando pagamento com cartão de crédito no valor de R$ " + String.format("%.2f", amount));
        return true;
    }

    @Override
    public String getTransactionDetails() {
        return "Pagamento realizado com Cartão de Crédito";
    }
} 