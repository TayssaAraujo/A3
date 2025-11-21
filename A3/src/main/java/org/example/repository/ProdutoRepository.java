package org.example.repository;

import org.example.entity.Produto;
import org.example.util.ConexaoBD;

import java.sql.*;

public class ProdutoRepository {

    // --- 1. Cadastrar (INSERT) - Retorna o ID gerado ---
    public int cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO PRODUTOS (codigo, nome, descricao, caracteristicas, quantidadeEstoque, estoqueMinimo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setString(4, produto.getCaracteristicas());
            stmt.setInt(5, produto.getQuantidadeEstoque());
            stmt.setInt(6, produto.getEstoqueMinimo());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retorna o id_produto
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
        }
        return -1; // Falha
    }

    // --- 2. Buscar por Código (SELECT) - Obtém o ID ---
    public Produto buscarProdutoPorCodigo(String codigo) {
        String sql = "SELECT id_produto, codigo, nome, descricao, caracteristicas, quantidadeEstoque, estoqueMinimo FROM PRODUTOS WHERE codigo = ?";
        Produto produto = null;

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    produto = new Produto(
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getString("descricao"),
                            rs.getString("caracteristicas"),
                            rs.getInt("quantidadeEstoque")
                    );
                    // Seta o ID do produto
                    produto.setId_produto(rs.getInt("id_produto"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
        return produto;
    }

    // --- 3. Atualizar Quantidade (UPDATE) ---
    public int atualizarQuantidadeEstoque(String codigo, int novoEstoque) throws SQLException {
        String sqlUpdate = "UPDATE PRODUTOS SET quantidadeEstoque = ? WHERE codigo = ?";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

            stmt.setInt(1, novoEstoque);
            stmt.setString(2, codigo);
            return stmt.executeUpdate();
        }
    }

    // --- 4. Registrar Movimentação (INSERT INTO MOVIMENTACAOESTOQUE) ---
    public void registrarMovimentacao(int idProduto, String tipo, int quantidade, String responsavel) {
        String sql = "INSERT INTO MOVIMENTACAOESTOQUE (id_produto, tipo_movimentacao, quantidade, responsavel) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduto);
            stmt.setString(2, tipo); // "ENTRADA" ou "SAIDA"
            stmt.setInt(3, quantidade);
            stmt.setString(4, responsavel);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao registrar movimentação: " + e.getMessage());
        }
    }
}