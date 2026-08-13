package com.nexusledger.core;

import com.nexusledger.exception.SaldoInsuficienteException; // Importação da nossa nova exceção
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaCorrenteTest {

    private ContaCorrente conta;

    @BeforeEach
    void prepararConta() {
        conta = new ContaCorrente("12345-6", "0001", "Luis Gustavo", 500.00);
    }

    @Test
    @DisplayName("Deve depositar valor corretamente e registrar no histórico")
    void testDepositarComSucesso() {
        conta.depositar(200.00);

        assertEquals(200.00, conta.getSaldo(), "O saldo deve ser exatamente 200.00 após o depósito");
        assertEquals(1, conta.getHistorico().size(), "Deve haver exatamente 1 transação no histórico");
        assertEquals("DEPÓSITO", conta.getHistorico().get(0).tipoOperacao());
    }

    @Test
    @DisplayName("Deve permitir saque usando o limite do Cheque Especial")
    void testSacarComChequeEspecial() {

        conta.depositar(100.00);
        conta.sacar(300.00);

        assertEquals(-200.00, conta.getSaldo(), "O saldo deve ficar negativo dentro do limite do cheque especial");
        assertEquals(2, conta.getHistorico().size(), "Deve registrar o depósito e o saque");
        assertEquals("SAQUE (CHEQUE ESPECIAL)", conta.getHistorico().get(1).tipoOperacao());
    }

    @Test
    @DisplayName("Deve bloquear saque que ultrapasse o saldo mais o limite")
    void testBloquearSaqueAcimaDoLimite() {

        // Substituímos o IllegalArgumentException pelo nosso SaldoInsuficienteException
        SaldoInsuficienteException excecao = assertThrows(SaldoInsuficienteException.class, () -> {
            conta.sacar(600.00);
        });

        // Atualizamos a mensagem para bater exatamente com a que está na ContaCorrente
        assertEquals("Saldo Insuficiente. Limite do Cheque Especial excedido.", excecao.getMessage());
    }

    @Test
    @DisplayName("Deve impedir modificação externa na lista de histórico (Imutabilidade)")
    void testImpedirLimpezaDoHistorico() {
        conta.depositar(100.00);

        assertThrows(UnsupportedOperationException.class, () -> {
            conta.getHistorico().clear();
        });
    }
}