package com.unicesumar.payment;

public class BoletoPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        // Simulação de processamento de pagamento com boleto
        System.out.println("Gerando boleto no valor de R$ " + String.format("%.2f", amount));
        return true;
    }

    @Override
    public String getTransactionDetails() {
        return "Pagamento realizado com Boleto";
    }
} 