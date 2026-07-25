package com.nexusledger.core;

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

    @Override
    public void sacar(double valor){
        if(valor <= 0){
            throw new IllegalArgumentException("O valor do saque deve ser maior que 0.");
        }
        else if(valor > this.saldo + this.limiteChequeES){
            throw new IllegalArgumentException("Saldo Insuficiente.");
        } else {
            this.saldo -= valor;
            Transacao transacao = new Transacao(UUID.randomUUID(), LocalDateTime.now(), "SAQUE (CHEQUE ESPECIAL)", valor);
            this.historico.add(transacao);
        }
    }
}
