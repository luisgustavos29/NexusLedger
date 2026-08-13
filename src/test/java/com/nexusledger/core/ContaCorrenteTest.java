package com.nexusledger.core;

import com.nexusledger.exception.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContaCorrenteTest {

    private ContaCorrente conta;

    @BeforeEach
    void prepararConta() {
        conta = new ContaCorrente("12345-6", "0001", "Luis", 500.00);
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
    @DisplayName("Deve aceitar descrição customizada via sobrecarga no depósito")
    void testDepositarComDescricaoCustomizada() {
        conta.depositar(150.00, "PIX RECEBIDO");
        assertEquals("PIX RECEBIDO", conta.getHistorico().get(0).tipoOperacao(), "A descrição customizada deve ser gravada no Record");
    }

    @Test
    @DisplayName("Deve realizar saque normal sem usar o limite e registrar como 'SAQUE'")
    void testSacarSemUsarLimite() {
        conta.depositar(500.00);
        conta.sacar(200.00); // Saldo final: 300 (Positivo)

        assertEquals(300.00, conta.getSaldo(), "O saldo deve ser 300.00 (Positivo)");
        assertEquals("SAQUE", conta.getHistorico().get(1).tipoOperacao(), "Se não entrou no cheque especial, a descrição deve ser apenas SAQUE");
    }

    @Test
    @DisplayName("Deve permitir saque usando o limite e avisar na descrição")
    void testSacarComChequeEspecial() {
        conta.depositar(100.00);
        conta.sacar(300.00); // Saldo final: -200

        assertEquals(-200.00, conta.getSaldo(), "O saldo deve ficar negativo dentro do limite do cheque especial");
        assertEquals(2, conta.getHistorico().size(), "Deve registrar o depósito e o saque");

        assertEquals("SAQUE (USO DO CHEQUE ESPECIAL)", conta.getHistorico().get(1).tipoOperacao());
    }

    @Test
    @DisplayName("Deve bloquear saque que ultrapasse o saldo mais o limite")
    void testBloquearSaqueAcimaDoLimite() {
        SaldoInsuficienteException excecao = assertThrows(SaldoInsuficienteException.class, () -> {
            conta.sacar(600.00);
        });

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