-- Tabela de Usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL
);

-- Tabela de Produtos
CREATE TABLE IF NOT EXISTS produtos (
    id TEXT PRIMARY KEY,
    nome TEXT NOT NULL,
    preco REAL NOT NULL
);

-- Tabela de Vendas
CREATE TABLE IF NOT EXISTS vendas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario_id INTEGER NOT NULL,
    valor_total REAL NOT NULL,
    forma_pagamento TEXT NOT NULL,
    detalhes_transacao TEXT,
    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de Itens da Venda
CREATE TABLE IF NOT EXISTS itens_venda (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    venda_id INTEGER NOT NULL,
    produto_id TEXT NOT NULL,
    preco REAL NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES vendas(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Inserir alguns dados de exemplo
INSERT INTO usuarios (nome, email) VALUES
    ('João da Silva', 'joao@example.com'),
    ('Maria Santos', 'maria@example.com');

INSERT INTO produtos (id, nome, preco) VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'Camiseta', 50.00),
    ('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'Calça Jeans', 150.00),
    ('6ba7b811-9dad-11d1-80b4-00c04fd430c8', 'Tênis', 200.00),
    ('6ba7b812-9dad-11d1-80b4-00c04fd430c8', 'Boné', 30.00); 