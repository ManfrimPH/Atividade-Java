package com.unicesumar.service;

import com.unicesumar.model.*;
import com.unicesumar.payment.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class VendaService {
    private static final String DB_URL = "jdbc:sqlite:database.sqlite";

    public List<Produto> listarProdutos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM produtos")) {
            
            while (rs.next()) {
                produtos.add(new Produto(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("nome"),
                    rs.getDouble("preco")
                ));
            }
        }
        return produtos;
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios ORDER BY id")) {
            
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email")
                ));
            }
        }
        return usuarios;
    }

    public Usuario buscarUsuarioPorEmail(String email) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios WHERE email = ?")) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("email")
                );
            }
            return null;
        }
    }

    public Produto buscarProdutoPorId(String id) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM produtos WHERE id = ?")) {
            
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Produto(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("nome"),
                    rs.getDouble("preco")
                );
            }
            return null;
        }
    }

    public void registrarVenda(Venda venda) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);
            try {
                // Inserir a venda
                try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO vendas (usuario_id, valor_total, forma_pagamento, detalhes_transacao) VALUES (?, ?, ?, ?)")) {
                    
                    stmt.setInt(1, venda.getUsuario().getId());
                    stmt.setDouble(2, venda.getValorTotal());
                    stmt.setString(3, venda.getFormaPagamento());
                    stmt.setString(4, venda.getDetalhesTransacao());
                    stmt.executeUpdate();

                    // Obter o ID da venda recém-inserida
                    try (Statement idStmt = conn.createStatement();
                         ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                        if (rs.next()) {
                            int vendaId = rs.getInt(1);
                            
                            // Inserir os itens da venda
                            try (PreparedStatement itemStmt = conn.prepareStatement(
                                "INSERT INTO itens_venda (venda_id, produto_id, preco) VALUES (?, ?, ?)")) {
                                
                                for (Produto produto : venda.getProdutos()) {
                                    itemStmt.setInt(1, vendaId);
                                    itemStmt.setString(2, produto.getId().toString());
                                    itemStmt.setDouble(3, produto.getPreco());
                                    itemStmt.executeUpdate();
                                }
                            }
                        }
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Produto> buscarProdutosPorIds(String[] ids) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        for (String id : ids) {
            Produto produto = buscarProdutoPorId(id.trim());
            if (produto != null) {
                produtos.add(produto);
            }
        }
        return produtos;
    }
} 