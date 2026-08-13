package com.nexusledger.service;

import com.nexusledger.core.ContaBancaria;
import com.nexusledger.exception.ValorInvalidoException;

import java.util.ArrayList;
import java.util.List;

public class BancoService {

    private List<ContaBancaria> contas;

    public BancoService() {
        this.contas = new ArrayList<>();
    }

    public void adicionarConta(ContaBancaria conta) {
        this.contas.add(conta);
    }

    public ContaBancaria buscarConta(String numeroDaConta) {

        for (ContaBancaria conta : contas) {

            if (conta.getNmrConta().equals(numeroDaConta)) {
                return conta;
            }
        }
        return null;
    }

    public void realizarPix(String numeroOrigem, String numeroDestino, double valor) {
        // TRAVA DO AUTO-PIX
        if (numeroOrigem.equals(numeroDestino)) {
            throw new ValorInvalidoException("PIX Recusado: Não é possível transferir para a própria conta.");
        }

        ContaBancaria origem = buscarConta(numeroOrigem);
        ContaBancaria destino = buscarConta(numeroDestino);

        if (origem == null || destino == null) {
            throw new ValorInvalidoException("PIX Recusado: Conta de origem ou destino não encontrada.");
        }

        origem.sacar(valor, "PIX ENVIADO");
        destino.depositar(valor, "PIX RECEBIDO");
    }
}