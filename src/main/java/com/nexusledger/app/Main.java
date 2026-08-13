package com.nexusledger.app;

import com.nexusledger.core.ContaBancaria;
import com.nexusledger.core.ContaCorrente;
import com.nexusledger.core.ContaPoupanca;
import com.nexusledger.core.Transacao;
import com.nexusledger.service.BancoService;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        BancoService banco = new BancoService();

        boolean rodando = true;
        ContaBancaria contaLogada = null;

        while (rodando) {
            if (contaLogada == null) {
                System.out.println("\n========== BEM-VINDO AO NEXUS LEDGER ==========");
                System.out.println("1. Criar Nova Conta");
                System.out.println("2. Acessar Conta (Login)");
                System.out.println("3. Sair do Sistema");
                System.out.print("Escolha uma opção: ");

                int opcao = teclado.nextInt();
                teclado.nextLine();

                switch (opcao) {
                    case 1:
                        System.out.println("\n--- ABERTURA DE CONTA ---");
                        System.out.print("Digite seu nome: ");
                        String nome = teclado.nextLine();
                        System.out.print("Digite o número desejado para a conta: ");
                        String numero = teclado.nextLine();

                        // Verifica se o número já existe!
                        if (banco.buscarConta(numero) != null) {
                            System.out.println(">> Erro: Já existe uma conta cadastrada com este número! Cadastro cancelado.");
                            break;
                        }

                        System.out.print("Digite a agência: ");
                        String agencia = teclado.nextLine();

                        System.out.print("Qual o tipo da conta? (1 - Corrente / 2 - Poupança): ");
                        int tipo = teclado.nextInt();
                        teclado.nextLine();

                        if (tipo == 1) {
                            ContaCorrente cc = new ContaCorrente(numero, agencia, nome, 500.0);
                            banco.adicionarConta(cc);
                            System.out.println(">> Conta Corrente criada com sucesso! Limite de R$ 500 liberado.");
                        } else if (tipo == 2) {
                            ContaPoupanca cp = new ContaPoupanca(numero, agencia, nome, 0.05);
                            banco.adicionarConta(cp);
                            System.out.println(">> Conta Poupança criada com sucesso! Rendimento de 5% ativado.");
                        } else {
                            System.out.println(">> Tipo inválido. Cadastro cancelado.");
                        }
                        break;

                    case 2:
                        System.out.print("\nDigite o número da sua conta para acessar: ");
                        String numeroLogin = teclado.nextLine();
                        ContaBancaria contaEncontrada = banco.buscarConta(numeroLogin);

                        if (contaEncontrada != null) {
                            contaLogada = contaEncontrada;
                            System.out.println(">> Login realizado com sucesso! Olá, " + contaLogada.getNomeTitular() + ".");
                        } else {
                            System.out.println(">> Erro: Conta não encontrada no sistema.");
                        }
                        break;

                    case 3:
                        rodando = false;
                        System.out.println(">> Encerrando o NexusLedger... Até logo!");
                        break;

                    default:
                        System.out.println(">> Opção inválida!");
                }
            }
            else {
                System.out.println("\n========== PAINEL DO CLIENTE ==========");
                System.out.println("Titular: " + contaLogada.getNomeTitular() + " | Conta: " + contaLogada.getNmrConta());
                System.out.println("1. Consultar Saldo");
                System.out.println("2. Consultar Extrato");
                System.out.println("3. Realizar Depósito");
                System.out.println("4. Realizar Saque");
                System.out.println("5. Realizar PIX");
                System.out.println("6. Aplicar Rendimento (Apenas Poupança)");
                System.out.println("7. Deslogar (Sair da Conta)");
                System.out.print("Escolha uma operação: ");

                int opcaoLogada = teclado.nextInt();
                teclado.nextLine();

                switch (opcaoLogada) {
                    case 1:
                        System.out.println(">> Saldo atual: R$ " + contaLogada.getSaldo());
                        break;

                    case 2:
                        System.out.println("\n--- EXTRATO BANCÁRIO ---");
                        if (contaLogada.getHistorico().isEmpty()) {
                            System.out.println(">> Nenhuma movimentação registrada.");
                        } else {
                            DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                            for (Transacao t : contaLogada.getHistorico()) {
                                String dataFormatada = t.timestamp().format(formatador);
                                System.out.printf("[%s] %-30s | Valor: R$ %.2f%n", dataFormatada, t.tipoOperacao(), t.valor());
                            }
                        }
                        break;

                    case 3:
                        System.out.print("Digite o valor a depositar: R$ ");
                        double valorDep = teclado.nextDouble();
                        teclado.nextLine();
                        try {
                            contaLogada.depositar(valorDep);
                            System.out.println(">> Depósito realizado com sucesso!");
                            System.out.println(">> Novo saldo: R$ " + contaLogada.getSaldo());
                        } catch (Exception e) {
                            System.out.println(">> Erro no depósito: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.print("Digite o valor para saque: R$ ");
                        double valorSaque = teclado.nextDouble();
                        teclado.nextLine();
                        try {
                            contaLogada.sacar(valorSaque);
                            System.out.println(">> Saque realizado com sucesso!");
                            System.out.println(">> Novo saldo: R$ " + contaLogada.getSaldo());

                            if (contaLogada.getSaldo() < 0) {
                                System.out.println(">> [ATENÇÃO]: Você está utilizando o Limite do Cheque Especial. Sujeito a juros mensais.");
                            }
                        } catch (Exception e) {
                            System.out.println(">> Falha no saque: " + e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.print("Número da conta destino: ");
                        String numDest = teclado.nextLine();
                        System.out.print("Valor do PIX: R$ ");
                        double valorPix = teclado.nextDouble();
                        teclado.nextLine();

                        try {
                            banco.realizarPix(contaLogada.getNmrConta(), numDest, valorPix);
                            System.out.println(">> PIX enviado com sucesso!");
                            System.out.println(">> Novo saldo: R$ " + contaLogada.getSaldo());

                            if (contaLogada.getSaldo() < 0) {
                                System.out.println(">> [ATENÇÃO]: Você está utilizando o Limite do Cheque Especial. Sujeito a juros mensais.");
                            }
                        } catch (Exception e) {
                            System.out.println(">> Falha no PIX: " + e.getMessage());
                        }
                        break;

                    case 6:
                        if (contaLogada instanceof ContaPoupanca) {
                            ContaPoupanca cp = (ContaPoupanca) contaLogada;
                            cp.renderJuros();
                            System.out.println(">> Juros aplicados com sucesso! Novo saldo: R$ " + cp.getSaldo());
                        } else {
                            System.out.println(">> Operação inválida: A sua conta não possui rendimento por juros.");
                        }
                        break;

                    case 7:
                        contaLogada = null;
                        System.out.println(">> Você saiu da sua conta com segurança.");
                        break;

                    default:
                        System.out.println(">> Opção inválida!");
                }
            }
        }
        teclado.close();
    }
}