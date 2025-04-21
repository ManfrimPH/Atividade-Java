package com.unicesumar.payment;

import java.util.UUID;

public class PixPayment implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        // Simulação de processamento de pagamento com PIX
        System.out.println("Processando pagamento PIX no valor de R$ " + String.format("%.2f", amount));
        return true;
    }

    @Override
    public String getTransactionDetails() {
        String chaveAutenticacao = UUID.randomUUID().toString();
        return "Pagamento confirmado com sucesso via PIX. Chave de Autenticação: " + chaveAutenticacao;
    }
} 