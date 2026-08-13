package com.nexusledger.core;

import com.nexusledger.exception.SaldoInsuficienteException;
import com.nexusledger.exception.ValorInvalidoException;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContaCorrente extends ContaBancaria{
    private double limiteChequeES;

    public ContaCorrente(String nmrConta, String agencia, String nomeTitular, double limiteChequeES) {
        super(nmrConta, agencia, nomeTitular);
        this.limiteChequeES = limiteChequeES;
    }

    @Override
    public double calcularTributos(){
        return this.saldo * 0.01;
    }

    public void sacar(double valor, String descricao){
        if(valor <= 0){
            throw new ValorInvalidoException("O valor do saque deve ser maior que 0.");
        }
        else if(valor > this.saldo + this.limiteChequeES){
            throw new SaldoInsuficienteException("Saldo Insuficiente. Limite do Cheque Especial excedido.");
        } else {
            this.saldo -= valor;

            String descricaoFinal = (this.saldo < 0) ? descricao + " (USO DO CHEQUE ESPECIAL)" : descricao;

            Transacao transacao = new Transacao(UUID.randomUUID(), LocalDateTime.now(), descricaoFinal, valor);
            this.historico.add(transacao);
        }
    }

    @Override
    public void sacar(double valor){
        sacar(valor, "SAQUE");
    }
}