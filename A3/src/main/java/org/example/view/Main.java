package org.example.view;

import org.example.service.GerenciadorDeEstoque;
import org.example.entity.Produto;
import org.example.util.ConexaoBD;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        GerenciadorDeEstoque service = new GerenciadorDeEstoque();
        Scanner scanner = new Scanner(System.in);
        String opcao;

        try {
            ConexaoBD.conectar();
        } catch (SQLException e) {
            System.err.println("Sistema inoperante. Falha na conexão com o BD. Verifique o MySQL e o ConexaoBD.");
            return;
        }

        System.out.println("Sistema de Estoque Diamante - Inicializado");

        while (true) {
            exibirMenu();
            opcao = scanner.nextLine();

            try {
                switch (opcao) {
                    case "1":
                        cadastrarNovoProduto(service, scanner);
                        break;
                    case "2":
                        consultarProduto(service, scanner);
                        break;
                    case "3":
                        darBaixa(service, scanner);
                        break;
                    case "4":
                        reporEstoque(service, scanner);
                        break;
                    case "0":
                        System.out.println("\n Encerrando o Sistema. Até logo!");
                        ConexaoBD.fecharConexao();
                        scanner.close();
                        return;
                    default:
                        System.out.println(" Opção inválida. Tente novamente (0 a 4).");
                }
            } catch (InputMismatchException e) {
                System.err.println("Erro de entrada. Por favor, digite um formato de dado válido.");
                scanner.nextLine();
            } catch (Exception e) {
                System.err.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n--- MENU DE ACESSO ---");
        System.out.println("1 - Cadastrar Novo Produto");
        System.out.println("2 - Buscar Produto por Código");
        System.out.println("3 - Dar Baixa em Estoque (Venda)");
        System.out.println("4 - Repor Estoque (Entrada de Material)");
        System.out.println("0 - Sair do Sistema");
        System.out.print("Escolha uma opção: ");
    }

    private static void cadastrarNovoProduto(GerenciadorDeEstoque service, Scanner scanner) {
        System.out.println("\n--- CADASTRO DE PRODUTO ---");
        System.out.print("Código (Ex: CHU005): ");
        String codigo = scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.print("Características (Ex: 220V, Branco): ");
        String caracteristicas = scanner.nextLine();

        int qtdInicial = 0;
        while (true) {
            try {
                System.out.print("Quantidade Inicial em Estoque: ");
                qtdInicial = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número inteiro para a quantidade.");
            }
        }

        Produto novoProduto = new Produto(codigo, nome, descricao, caracteristicas, qtdInicial);
        service.cadastrarProduto(novoProduto);
    }

    private static void consultarProduto(GerenciadorDeEstoque service, Scanner scanner) {
        System.out.println("\n--- CONSULTA DE PRODUTO ---");
        System.out.print("Digite o Código do Produto: ");
        String codigo = scanner.nextLine();

        Produto produto = service.buscarProdutoPorCodigo(codigo);

        if (produto != null) {
            System.out.println("\nProduto Encontrado:");
            System.out.println("  Código: " + produto.getCodigo());
            System.out.println("  ID Interno (PK): " + produto.getId_produto());
            System.out.println("  Nome: " + produto.getNome());
            System.out.println("  Descrição: " + produto.getDescricao());
            System.out.println("  Características: " + produto.getCaracteristicas());
            System.out.println("  Estoque Atual: " + produto.getQuantidadeEstoque() + " unidades");
            System.out.println("  Estoque Mínimo: " + produto.getEstoqueMinimo() + " unidades");
        } else {
            System.out.println("Produto com código " + codigo + " não encontrado.");
        }
    }

    private static void darBaixa(GerenciadorDeEstoque service, Scanner scanner) {
        System.out.println("\n--- BAIXA DE ESTOQUE (VENDA) ---");
        System.out.print("Digite o Código do Produto vendido: ");
        String codigo = scanner.nextLine();

        int quantidadeBaixa = 0;
        while (true) {
            try {
                System.out.print("Digite a Quantidade Vendida: ");
                quantidadeBaixa = Integer.parseInt(scanner.nextLine());
                if (quantidadeBaixa <= 0) {
                    System.out.println("A quantidade deve ser maior que zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número inteiro para a quantidade.");
            }
        }

        service.darBaixaEmEstoque(codigo, quantidadeBaixa);
    }

    private static void reporEstoque(GerenciadorDeEstoque service, Scanner scanner) {
        System.out.println("\n--- REPOSIÇÃO DE ESTOQUE ---");
        System.out.print("Digite o Código do Produto a ser reposto: ");
        String codigo = scanner.nextLine();

        int quantidadeReposta = 0;
        while (true) {
            try {
                System.out.print("Digite a Quantidade Recebida: ");
                quantidadeReposta = Integer.parseInt(scanner.nextLine());
                if (quantidadeReposta <= 0) {
                    System.out.println("A quantidade deve ser maior que zero.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, digite um número inteiro para a quantidade.");
            }
        }

        service.reporEstoque(codigo, quantidadeReposta);
    }
}