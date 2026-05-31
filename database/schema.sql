CREATE DATABASE IF NOT EXISTS mercadinho
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE mercadinho;

DROP TABLE IF EXISTS item_nota;
DROP TABLE IF EXISTS nota_compra;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS tipo_produto;
DROP TABLE IF EXISTS usuario;

CREATE TABLE tipo_produto (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nome    VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE produto (
    id_produto         INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras      VARCHAR(13) NOT NULL UNIQUE,
    nome               VARCHAR(150) NOT NULL,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    preco              DECIMAL(10,2) NOT NULL,
    forma_venda        ENUM('QUILO','UNIDADE') NOT NULL,
    id_tipo            INT,
    CONSTRAINT fk_produto_tipo FOREIGN KEY (id_tipo) REFERENCES tipo_produto(id_tipo)
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome       VARCHAR(100) NOT NULL,
    senha      VARCHAR(100) NOT NULL,
    perfil     ENUM('GERENTE','CAIXA') NOT NULL
);

CREATE TABLE nota_compra (
    numero_nota INT AUTO_INCREMENT PRIMARY KEY,
    data_hora   DATE NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0
);

CREATE TABLE item_nota (
    id_item        INT AUTO_INCREMENT PRIMARY KEY,
    numero_nota    INT NOT NULL,
    id_produto     INT NOT NULL,
    quantidade     INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_nota    FOREIGN KEY (numero_nota) REFERENCES nota_compra(numero_nota),
    CONSTRAINT fk_item_produto FOREIGN KEY (id_produto)  REFERENCES produto(id_produto)
);

INSERT INTO usuario (nome, senha, perfil) VALUES
    ('admin', '1234', 'GERENTE'),
    ('caixa1', '1234', 'CAIXA');

INSERT INTO tipo_produto (nome) VALUES ('Alimentos'), ('Bebidas');
