package com.nexusledger.core;

import com.nexusledger.exception.SaldoInsuficienteException; // Importação da nossa nova exceção
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaPoupancaTest {

    private ContaPoupanca conta;

    @BeforeEach
    void prepararConta() {
        conta = new ContaPoupanca("54321-0", "0001", "Luis Gustavo", 0.05);
    }

    @Test
    @DisplayName("Deve render juros corretamente sobre o saldo positivo")
    void testRenderJurosComSucesso() {
        conta.depositar(1000.00);

        conta.renderJuros();

        assertEquals(1050.00, conta.getSaldo(), "O saldo deve ser 1050.00 após render juros");
    }

    @Test
    @DisplayName("Deve ser 100% isenta de tributos (Retornar 0.0)")
    void testIsencaoDeTributos() {
        conta.depositar(5000.00);
        assertEquals(0.0, conta.calcularTributos(), "Conta Poupança deve ser isenta e retornar 0.0 de tributos");
    }

    @Test
    @DisplayName("Deve bloquear saque maior que o saldo (Não possui Cheque Especial)")
    void testBloquearSaqueSemSaldo() {
        conta.depositar(100.00);

        // Substituímos o IllegalArgumentException pelo nosso SaldoInsuficienteException
        SaldoInsuficienteException excecao = assertThrows(SaldoInsuficienteException.class, () -> {
            conta.sacar(150.00);
        });

        // Atualizamos a mensagem para bater exatamente com a que está na ContaBancaria mãe
        assertEquals("Saldo Insuficiente para realizar o saque.", excecao.getMessage());
    }
}