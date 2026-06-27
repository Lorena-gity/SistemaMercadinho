# Mercadinho do Seu Pedrinho

Sistema de mercadinho (PDV e gestão) feito em JavaFX com banco de dados MySQL.

## Como rodar

1. Subir o banco de dados (MySQL pelo Docker):

   docker compose up -d

2. Rodar a aplicação pela IDE (executando a classe `Login`) ou pelo Maven:

   mvnw javafx:run

## Login de teste

- admin / 1234 (gerente)
- caixa1 / 1234 (caixa)

## Banco de dados

O banco roda no Docker na porta 3309 (configurado no `docker-compose.yml`).
O script `database/schema.sql` cria as tabelas na primeira vez que o container sobe.
Para zerar os dados: `docker compose down -v` e suba de novo.
