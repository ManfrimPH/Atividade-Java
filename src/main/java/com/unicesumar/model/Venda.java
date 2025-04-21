package com.unicesumar.model;

import java.util.List;
import java.time.LocalDateTime;

public class Venda {
    private int id;
    private Usuario usuario;
    private List<Produto> produtos;
    private double valorTotal;
    private String formaPagamento;
    private LocalDateTime dataVenda;
    private String detalhesTransacao;

    public Venda(Usuario usuario, List<Produto> produtos, String formaPagamento, String detalhesTransacao) {
        this.usuario = usuario;
        this.produtos = produtos;
        this.formaPagamento = formaPagamento;
        this.detalhesTransacao = detalhesTransacao;
        this.dataVenda = LocalDateTime.now();
        this.valorTotal = calcularValorTotal();
    }

    private double calcularValorTotal() {
        return produtos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public String getDetalhesTransacao() {
        return detalhesTransacao;
    }
} 