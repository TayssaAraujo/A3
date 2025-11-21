package org.example.service;

import org.example.entity.Produto;
import org.example.repository.ProdutoRepository;
import java.sql.SQLException;

public class GerenciadorDeEstoque {

    private final ProdutoRepository repository = new ProdutoRepository();

    // 1. Cadastro
    public void cadastrarProduto(Produto produto) {
        int idGerado = repository.cadastrarProduto(produto);
        if (idGerado > 0) {
            // Se o cadastro foi bem-sucedido, registra a entrada inicial
            repository.registrarMovimentacao(idGerado, "ENTRADA", produto.getQuantidadeEstoque(), "Sistema - Cadastro Inicial");
            System.out.println("Movimentação de estoque inicial registrada.");
        }
    }

    // 2. Consulta
    public Produto buscarProdutoPorCodigo(String codigo) {
        return repository.buscarProdutoPorCodigo(codigo);
    }

    // 3. Dar Baixa (Venda) - Inclui Registro de Movimentação
    public void darBaixaEmEstoque(String codigo, int quantidadeBaixa) {
        Produto produto = buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            System.out.println("ERRO: Produto com código " + codigo + " não encontrado.");
            return;
        }

        int estoqueAtual = produto.getQuantidadeEstoque();
        if (quantidadeBaixa > estoqueAtual) {
            System.out.println("ERRO: Baixa de " + quantidadeBaixa + " unidades é maior que o estoque atual de " + estoqueAtual + ".");
            return;
        }

        int novoEstoque = estoqueAtual - quantidadeBaixa;

        try {
            int linhasAfetadas = repository.atualizarQuantidadeEstoque(codigo, novoEstoque);

            if (linhasAfetadas > 0) {
                // REGISTRA MOVIMENTAÇÃO DE SAÍDA
                repository.registrarMovimentacao(produto.getId_produto(), "SAIDA", quantidadeBaixa, "Balconista Padrão");

                System.out.printf("Baixa efetuada: %d unidades do produto %s. Novo estoque: %d.\n",
                        quantidadeBaixa, produto.getNome(), novoEstoque);

                // Lógica de Negócio: Verifica o alerta
                produto.setQuantidadeEstoque(novoEstoque);
                verificarEstoqueMinimo(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao dar baixa no estoque: " + e.getMessage());
        }
    }

    // 4. Reposição de Estoque (Entrada) - Inclui Registro de Movimentação
    public void reporEstoque(String codigo, int quantidadeReposta) {
        Produto produto = buscarProdutoPorCodigo(codigo);

        if (produto == null) {
            System.out.println("ERRO: Produto com código " + codigo + " não encontrado.");
            return;
        }

        int novoEstoque = produto.getQuantidadeEstoque() + quantidadeReposta;

        try {
            int linhasAfetadas = repository.atualizarQuantidadeEstoque(codigo, novoEstoque);

            if (linhasAfetadas > 0) {
                // REGISTRA MOVIMENTAÇÃO DE ENTRADA
                repository.registrarMovimentacao(produto.getId_produto(), "ENTRADA", quantidadeReposta, "Balconista Padrão");

                System.out.printf("⬆️ Reposição efetuada: Adicionadas %d unidades do produto %s. Novo estoque: %d.\n",
                        quantidadeReposta, produto.getNome(), novoEstoque);
            }
        } catch (SQLException e) {
            System.err.println(" Erro ao repor o estoque: " + e.getMessage());
        }
    }

    private void verificarEstoqueMinimo(Produto produto) {
        if (produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo()) {
            System.out.println("\n" +
                    "ALERTA DE ESTOQUE MÍNIMO: O produto " + produto.getNome() +
                    " (Cód: " + produto.getCodigo() + ") atingiu " +
                    produto.getQuantidadeEstoque() + " unidades. Reposição Urgente!" +
                    "\n");
        }
    }
}