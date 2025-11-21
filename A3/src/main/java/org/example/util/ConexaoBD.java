package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:mysql://localhost:3306/estoque_diamante";
    private static final String USUARIO = "root";
    private static final String SENHA = "Assyat.123";
    private static Connection conexao = null;

    public static Connection conectar() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o BD estabelecida com sucesso.");
            } catch (SQLException e) {
                System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
                throw e;
            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC não encontrado. Verifique o pom.xml.");
                throw new SQLException("Driver JDBC não encontrado.");
            }
        }
        return conexao;
    }

    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                System.out.println("Conexão com o BD fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}