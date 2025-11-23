
# Sistema de Gerenciamento de Estoque Diamante

## 1\. Descrição do Projeto

Este projeto consiste em um sistema de backend em **Java**, utilizando a arquitetura em camadas, com persistência de dados em um banco de dados **MySQL**. O foco é o módulo de **Controle de Estoque** de um comércio, implementando regras de negócio e rastreabilidade.

### Requisitos Funcionais Implementados:

  * **Cadastro de Produtos:** Armazenamento de informações básicas e estoque inicial.
  * **Controle de Estoque:** Atualização da quantidade de estoque após entradas ou saídas.
  * **Alerta de Estoque Mínimo:** Dispara um alerta quando a quantidade em estoque atinge ou é inferior ao valor **3**.
  * **Controle de Movimentação:** Registro de cada transação (entrada/saída) na tabela `MOVIMENTACAOESTOQUE` para rastreabilidade e histórico.
  * **Interfaces:** O sistema oferece uma interface de **Console (`Main.java`)**.

-----

## 2\. Arquitetura e Estrutura do Código

O projeto segue o padrão de desenvolvimento em camadas, com todas as classes Java divididas nos seguintes pacotes, localizados em `src/main/java/org/example/`:

| Pacote | Camada | Classes | Responsabilidade |
| :--- | :--- | :--- | :--- |
| `org.example.entity` | **Entity (Modelo)** | `Produto.java` | Mapeamento dos objetos de negócio (`id_produto`, `codigo`, etc.). |
| `org.example.repository` | **Repository (DAO)** | `ProdutoRepository.java` | Contém todo o código SQL para CRUD e registro de movimentações. |
| `org.example.service` | **Service (Negócio)** | `GerenciadorDeEstoque.java` | Contém a **Lógica de Negócio** (verificação de Estoque Mínimo) e orquestração. |
| `org.example.util` | **Utility** | `ConexaoBD.java` | Gerencia a conexão com o banco de dados. |
| `org.example.view` | **View/Controller** | `Main.java`, `SistemaEstoqueGUI.java` | Interfaces de usuário e controle de fluxo da aplicação. |

-----

## 3\. Configuração do Banco de Dados (MySQL)

O sistema utiliza o banco de dados MySQL com o nome `estoque_diamante`.

### 3.1. Credenciais de Conexão

As credenciais estão configuradas e confirmadas na classe `org.example.util.ConexaoBD.java`:

| Variável | Valor |
| :--- | :--- |
| **URL** | `jdbc:mysql://localhost:3306/estoque_diamante` |
| **USUARIO** | `Seu_usuario` |
| **SENHA** | `Sua_senha` |

### 3.2. Script SQL de Criação

Execute este código no seu MySQL Workbench antes de rodar o código Java.

```sql
-- 1. ESTRUTURA DO BANCO DE DADOS
CREATE DATABASE IF NOT EXISTS estoque_diamante
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;

-- Direciona o sistema a usar o bando de dados criado
USE estoque_diamante;

-- Limpa as tabelas existentes para garantir a nova estrutura
DROP TABLE IF EXISTS MOVIMENTACAOESTOQUE;
DROP TABLE IF EXISTS PRODUTOS;

-- 2. TABELA PRODUTOS (ENTIDADE CENTRAL - PK INT e UNIQUE Codigo)
CREATE TABLE PRODUTOS (
    id_produto INT PRIMARY KEY AUTO_INCREMENT, 
    codigo VARCHAR(50) UNIQUE NOT NULL COMMENT 'Código de barras/referência do produto (Chave de Negócio)',
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    caracteristicas VARCHAR(255),
    quantidadeEstoque INT NOT NULL,
    estoqueMinimo INT NOT NULL COMMENT 'Valor fixo em 3, usado para alertas'
);

-- 3. TABELA MOVIMENTACAOESTOQUE (CONTROLE E RASTREABILIDADE)
CREATE TABLE MOVIMENTACAOESTOQUE (
    id_movimentacao INT PRIMARY KEY AUTO_INCREMENT,
    id_produto INT NOT NULL,
    tipo_movimentacao ENUM('ENTRADA', 'SAIDA') NOT NULL COMMENT 'Tipo da operação: ENTRADA ou SAIDA',
    quantidade INT NOT NULL,
    data_movimentacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responsavel VARCHAR(100) NOT NULL COMMENT 'Usuário que realizou a operação',
    FOREIGN KEY (id_produto) REFERENCES PRODUTOS(id_produto)
);
```

-----

## 4\. Como Rodar o Projeto

### 4.1. Pré-requisitos

1.  **MySQL Server:** Servidor MySQL rodando.
2.  **Java JDK:** Versão 11 ou superior.
3.  **Dependência JDBC:** A dependência `mysql-connector-java` deve estar configurada no seu projeto.

### 4.2. Execução (Interface de Console)

1.  **Obter o Código:** Clone o repositório Git.
2.  **Configurar o BD:** Execute o script SQL da Seção 3.2 no seu MySQL.
3.  **Executar o `Main.java`:** Abra sua IDE (IntelliJ) e execute o método `main()` da classe **`org.example.view.Main.java`**.
4.  O sistema tentará a conexão e exibirá o Menu de Console para operações de CRUD, Baixa e Reposição.

## 5 Teste de conexão com banco de dados 

<img width="1500" height="592" alt="image" src="https://github.com/user-attachments/assets/7b328829-672b-4c46-83b8-08bc57fac174" />
<img width="1491" height="680" alt="image" src="https://github.com/user-attachments/assets/59bbd767-aeaa-4ad8-a646-33806d932a6d" />

## 5.1 Link do Sistema de Gestão e apresentação com video do sistema rodando
APRESENTAÇÂO CANVA: https://www.canva.com/design/DAG5b3ohf20/oPaLRcbsimcmrFHDuRos8Q/edit?utm_content=DAG5b3ohf20&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton

