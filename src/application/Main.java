package application;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import model.entities.Cliente;
import model.entities.ItemVenda;
import model.entities.Pago;
import model.entities.TipoPagamento;
import model.entities.Venda;
import model.services.Boleto;
import model.services.CartaoCredito;
import model.services.FormaPagamento;
import model.services.Pix;
import model.services.ServicoPagamento;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite seu nome: ");
        String nomeCliente = scanner.nextLine();

        System.out.print("Digite seu email: ");
        String emailCliente = scanner.nextLine();
        
        System.out.println("Cliente cadastrado com sucesso. Seja bem-vindo!");

        Cliente cliente = new Cliente(nomeCliente, emailCliente);

        var itemVendas = new ArrayList<ItemVenda>();
        ItemVenda iv;

        Venda venda = new Venda();
        venda.setCliente(cliente);

        int escolha = 0;

        try {
            while (true) {
                System.out.println("1 - Adicionar item no carrinho");
                System.out.println("2 - Remover item no carrinho");
                System.out.println("3 - Mostrar o sub total da compra");
                System.out.println("4 - Realizar pagamento");
                System.out.println("5 - Sair");

                System.out.print("Escolha uma opcao: ");
                escolha = scanner.nextInt();
                scanner.nextLine(); 

                switch (escolha) {

                    case 1:
                        System.out.print("Informe o número de identificação do produto (ou digite -1 para sair): ");
                        Integer numProduto = scanner.nextInt();

                        if (numProduto == -1) {
                            break;
                        }

                        System.out.print("Informe o nome do produto: ");
                        scanner.nextLine();
                        String nomeProduto = scanner.nextLine();

                        System.out.print("Informe o preço do produto: ");
                        Double precoProduto = scanner.nextDouble();

                        System.out.print("Informe a quantidade: ");
                        Integer quantProd = scanner.nextInt();

                        iv = new ItemVenda(numProduto, nomeProduto, precoProduto, quantProd);
                        itemVendas.add(iv);
                        venda.addItem(iv);
                        break;

                    case 2:
                        if (itemVendas.isEmpty()) {
                            System.out.println("O carrinho está vazio. Adicione itens primeiro.");
                            break;
                        }

                        System.out.print("Informe o nome do produto que você deseja remover: ");
                        scanner.nextLine();
                        nomeProduto = scanner.nextLine();

                        ItemVenda itemRemover = null;
                        for (ItemVenda item : itemVendas) {
                            if (item.getNome().equalsIgnoreCase(nomeProduto)) {
                                itemRemover = item;
                                break;
                            }
                        }

                        if (itemRemover != null) {
                            itemVendas.remove(itemRemover);
                            venda.removerItem(itemRemover);
                            System.out.println("Produto removido com sucesso.");
                        } else {
                            System.out.println("Produto não encontrado no carrinho.");
                        }

                        System.out.println("Produtos restantes no carrinho: ");
                        for (ItemVenda item : itemVendas) {
                            System.out.println(item);
                        }
                        break;

                    case 3:
                    	if (itemVendas.isEmpty()) {
                            System.out.println("O carrinho está vazio. Adicione itens primeiro.");
                            break;
                        }

                        double subtotal = venda.subTotal();

                        System.out.printf("Subtotal da compra: %.2f%n", subtotal);

                        if (venda.getParcelas() != null && venda.getParcelas() > 0) {
                            System.out.println("Detalhes das Parcelas:");

                            Double valorParcela = subtotal / venda.getParcelas();
                            LocalDate dataPagamento = LocalDate.now().plusMonths(1);

                            for (int i = 1; i <= venda.getParcelas(); i++) {
                                System.out.printf("Parcela #%d: %.2f - Data de Pagamento: %s%n", i, valorParcela, dataPagamento);
                                dataPagamento = dataPagamento.plusMonths(1);
                            }
                        }
                        break;

                    case 4:
                        if (itemVendas.isEmpty()) {
                            System.out.println("O carrinho está vazio. Adicione itens primeiro.");
                            break;
                        }

                        System.out.println("Escolha a forma de pagamento:");
                        System.out.println("1 - Boleto");
                        System.out.println("2 - Cartão de Crédito");
                        System.out.println("3 - PIX");
                        System.out.print("Digite o número da opção desejada: ");
                        int opcaoPagamento = scanner.nextInt();

                        Pago pago;
                        TipoPagamento tipoPagamento;
                        FormaPagamento servicoPagamento;

                        switch (opcaoPagamento) {
                            case 1:
                                pago = Pago.AGUARDO;
                                tipoPagamento = TipoPagamento.BOLETO;
                                servicoPagamento = new Boleto();
                                break;
                            case 2:
                                pago = Pago.AGUARDO;
                                tipoPagamento = TipoPagamento.CARTAO;
                                servicoPagamento = new CartaoCredito();
                                break;
                            case 3:
                                pago = Pago.AGUARDO;
                                tipoPagamento = TipoPagamento.PIX;
                                servicoPagamento = new Pix();
                                break;
                            default:
                                System.out.println("Opção inválida.");
                                return;
                        }

                        int numeroParcelas;
                        do {
                            System.out.print("Informe a quantidade de parcelas (deve ser maior que zero): ");
                            numeroParcelas = scanner.nextInt();
                        } while (numeroParcelas <= 0);

                        venda.setParcelas(numeroParcelas);
                        ServicoPagamento servico = new ServicoPagamento(servicoPagamento);
                        servico.processaPagamento(venda);
                        break;

                    case 5:
                        System.out.println("Saindo do programa. Obrigado!");
                        return;

                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("ERRO: digite um valor válido!");
        } finally {
            scanner.close();
        }
    }
}
