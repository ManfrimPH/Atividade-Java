package com.unicesumar;

import com.unicesumar.model.*;
import com.unicesumar.service.VendaService;
import com.unicesumar.payment.*;
import com.unicesumar.util.DatabaseInitializer;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final VendaService vendaService = new VendaService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Inicializar banco de dados
        DatabaseInitializer.initializeDatabase();

        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = Integer.parseInt(scanner.nextLine());

            try {
                switch (opcao) {
                    case 1:
                        listarProdutos();
                        break;
                    case 2:
                        listarUsuarios();
                        break;
                    case 3:
                        buscarUsuarioPorEmail();
                        break;
                    case 4:
                        buscarProdutoPorId();
                        break;
                    case 5:
                        realizarVenda();
                        break;
                    case 6:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
                e.printStackTrace();
            }
        } while (opcao != 6);
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n=== SISTEMA DE E-COMMERCE ===");
        System.out.println("1 - Listar Produtos");
        System.out.println("2 - Listar Usuários");
        System.out.println("3 - Buscar Usuário por Email");
        System.out.println("4 - Buscar Produto por ID");
        System.out.println("5 - Realizar Venda");
        System.out.println("6 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void listarProdutos() throws Exception {
        System.out.println("\n=== PRODUTOS DISPONÍVEIS ===");
        List<Produto> produtos = vendaService.listarProdutos();
        for (Produto produto : produtos) {
            System.out.printf("ID: %s - %s (R$ %.2f)%n", 
                produto.getId(), produto.getNome(), produto.getPreco());
        }
    }

    private static void listarUsuarios() throws Exception {
        System.out.println("\n=== USUÁRIOS CADASTRADOS ===");
        List<Usuario> usuarios = vendaService.listarUsuarios();
        for (Usuario usuario : usuarios) {
            System.out.printf("ID: %d - %s (Email: %s)%n", 
                usuario.getId(), usuario.getNome(), usuario.getEmail());
        }
    }

    private static void buscarUsuarioPorEmail() throws Exception {
        System.out.println("\n=== BUSCAR USUÁRIO POR EMAIL ===");
        System.out.print("Digite o email do usuário: ");
        String email = scanner.nextLine();
        
        Usuario usuario = vendaService.buscarUsuarioPorEmail(email);
        if (usuario != null) {
            System.out.println("\nUsuário encontrado:");
            System.out.printf("ID: %d%nNome: %s%nEmail: %s%n",
                usuario.getId(), usuario.getNome(), usuario.getEmail());
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    private static void buscarProdutoPorId() throws Exception {
        System.out.println("\n=== BUSCAR PRODUTO POR ID ===");
        System.out.print("Digite o ID do produto: ");
        String id = scanner.nextLine();
        
        Produto produto = vendaService.buscarProdutoPorId(id);
        if (produto != null) {
            System.out.println("\nProduto encontrado:");
            System.out.printf("ID: %s%nNome: %s%nPreço: R$ %.2f%n",
                produto.getId(), produto.getNome(), produto.getPreco());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void realizarVenda() throws Exception {
        // Buscar usuário
        Usuario usuario = null;
        while (usuario == null) {
            System.out.print("\nDigite o Email do usuário: ");
            String email = scanner.nextLine();
            usuario = vendaService.buscarUsuarioPorEmail(email);
            if (usuario == null) {
                System.out.println("Usuário não encontrado. Tente novamente.");
            } else {
                System.out.println("Usuário encontrado: " + usuario.getNome());
            }
        }

        // Buscar produtos
        List<Produto> produtos = null;
        while (produtos == null || produtos.isEmpty()) {
            System.out.print("Digite os IDs dos produtos (separados por vírgula): ");
            String[] produtoIds = scanner.nextLine().split(",");
            produtos = vendaService.buscarProdutosPorIds(produtoIds);
            
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado. Tente novamente.");
            } else {
                System.out.println("Produtos encontrados:");
                double total = 0;
                for (Produto produto : produtos) {
                    System.out.printf("- %s (R$ %.2f)%n", produto.getNome(), produto.getPreco());
                    total += produto.getPreco();
                }
                System.out.printf("Total: R$ %.2f%n", total);
            }
        }

        // Escolher forma de pagamento
        System.out.println("\nEscolha a forma de pagamento:");
        System.out.println("1 - Cartão de Crédito");
        System.out.println("2 - Boleto");
        System.out.println("3 - PIX");
        System.out.print("Opção: ");
        int opcaoPagamento = Integer.parseInt(scanner.nextLine());

        // Processar pagamento
        PaymentStrategy pagamento = PaymentFactory.createPayment(opcaoPagamento);
        System.out.println("\nAguarde, efetuando pagamento...");
        
        double valorTotal = produtos.stream().mapToDouble(Produto::getPreco).sum();
        if (pagamento.processPayment(valorTotal)) {
            String detalhesTransacao = pagamento.getTransactionDetails();
            System.out.println(detalhesTransacao);

            // Criar e registrar a venda
            String formaPagamento = opcaoPagamento == 1 ? "CARTAO" : 
                                  opcaoPagamento == 2 ? "BOLETO" : "PIX";
            
            Venda venda = new Venda(usuario, produtos, formaPagamento, detalhesTransacao);
            vendaService.registrarVenda(venda);

            // Exibir resumo
            System.out.println("\nResumo da venda:");
            System.out.println("Cliente: " + usuario.getNome());
            System.out.println("Produtos:");
            for (Produto produto : produtos) {
                System.out.println("- " + produto.getNome());
            }
            System.out.printf("Valor total: R$ %.2f%n", valorTotal);
            System.out.println("Pagamento: " + formaPagamento);
            System.out.println("\nVenda registrada com sucesso!");
        } else {
            System.out.println("Erro ao processar o pagamento.");
        }
    }
}
