package com.nexusledger.core;

import com.nexusledger.exception.SaldoInsuficienteException;
import com.nexusledger.exception.ValorInvalidoException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract class ContaBancaria {
    private String nmrConta;
    private String agencia;
    private String nomeTitular;
    protected double saldo;
    protected List<Transacao> historico;

    public ContaBancaria(String nmrConta, String agencia, String nomeTitular) {
        this.nmrConta = nmrConta;
        this.agencia = agencia;
        this.nomeTitular = nomeTitular;
        this.historico = new ArrayList<>();
    }

    public String getNmrConta() {
        return nmrConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getAgencia() {
        return agencia;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public List<Transacao> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public void depositar(double valor, String descricao){
        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do depósito deve ser maior que zero.");
        } else {
            saldo += valor;
            Transacao transacao = new Transacao(UUID.randomUUID(), LocalDateTime.now(), descricao, valor);
            this.historico.add(transacao);
        }
    }

    public void depositar(double valor){
        depositar(valor, "DEPÓSITO");
    }


    public void sacar(double valor, String descricao){
        if(valor <= 0){
            throw new ValorInvalidoException("O valor do saque deve ser maior que 0.");
        } else if (valor > saldo){
            throw new SaldoInsuficienteException("Saldo Insuficiente para realizar o saque.");
        } else {
            saldo -= valor;
            Transacao transacao = new Transacao(UUID.randomUUID(), LocalDateTime.now(), descricao, valor);
            this.historico.add(transacao);
        }
    }

    public void sacar(double valor){
        sacar(valor, "SAQUE");
    }

    public abstract double calcularTributos();
}