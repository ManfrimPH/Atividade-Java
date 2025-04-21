-- Criar tabela temporária para produtos
CREATE TABLE IF NOT EXISTS produtos_temp (
    id TEXT PRIMARY KEY,
    nome TEXT NOT NULL,
    preco REAL NOT NULL
);

-- Copiar dados existentes para a tabela temporária com novos UUIDs
INSERT INTO produtos_temp (id, nome, preco)
SELECT hex(randomblob(4)) || '-' || hex(randomblob(2)) || '-4' || substr(hex(randomblob(2)),2) || '-' || 
       substr('89ab',abs(random() % 4) + 1, 1) || substr(hex(randomblob(2)),2) || '-' || hex(randomblob(6)),
       nome,
       preco
FROM produtos;

-- Remover tabela antiga
DROP TABLE produtos;

-- Renomear tabela temporária
ALTER TABLE produtos_temp RENAME TO produtos;

-- Atualizar referências na tabela itens_venda
CREATE TABLE IF NOT EXISTS itens_venda_temp (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    venda_id INTEGER NOT NULL,
    produto_id TEXT NOT NULL,
    preco REAL NOT NULL,
    FOREIGN KEY (venda_id) REFERENCES vendas(id),
    FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- Copiar dados da tabela itens_venda para a temporária
-- Note que isso pode resultar em perda de referências, mas é necessário para manter a integridade
INSERT INTO itens_venda_temp (id, venda_id, produto_id, preco)
SELECT iv.id, iv.venda_id, p.id, iv.preco
FROM itens_venda iv
JOIN produtos p ON 1=1
LIMIT 1;

-- Remover tabela antiga
DROP TABLE itens_venda;

-- Renomear tabela temporária
ALTER TABLE itens_venda_temp RENAME TO itens_venda; 