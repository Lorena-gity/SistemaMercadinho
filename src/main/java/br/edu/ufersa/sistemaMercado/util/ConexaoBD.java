package br.edu.ufersa.sistemaMercado.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    private static final String URL = "jdbc:mysql://localhost:3306/mercadinho?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "mercado"; // use root ou usuário específico
    private static final String SENHA = "senhapoo"; // troque pela sua senha do MySQL

    private ConexaoBD() {}

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }
}
