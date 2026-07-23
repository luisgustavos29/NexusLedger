package com.nexusledger.core;

public abstract class ContaBancaria {
    private String nmrConta;
    private String agencia;
    private String nomeTitular;
    protected double saldo;

    //Não passa o saldo como paramêtro, pois toda nova conta se inicia com saldo zerado
    public ContaBancaria(String nmrConta, String agencia, String nomeTitular) {
        this.nmrConta = nmrConta;
        this.agencia = agencia;
        this.nomeTitular = nomeTitular;
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

    //!!! SEGURANÇA SETTERS = Em um sistema bancário seguro,
    // o saldo só muda através de transações (saque/depósito),
    // número de uma conta nunca muda depois de aberta.
    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    // THROW NEW para que o erro realmente pare a execução do metodo,
    // bloqueie a transação e avise o sistema que algo deu errado

    public void depositar(double valor){
        if (valor<=0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        } else {
            saldo += valor;
        }
    }

    public void sacar (double valor){
        if(valor<=0){
            throw new IllegalArgumentException("O valor do saque deve ser maior que 0.");
        } else if (valor > saldo){
            throw new IllegalArgumentException("Saldo Insuficiente.");
        } else {
            saldo -= valor;
        }
    }
     //OBRIGA cada subclasse (ContaCorrente, ContaPoupanca)
    // a implementar a sua própria lógica de cálculo, permitindo o uso do Polimorfismo no sistema.
    public abstract double calcularTributos();
}
