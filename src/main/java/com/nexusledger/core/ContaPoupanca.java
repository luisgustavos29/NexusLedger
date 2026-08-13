package com.nexusledger.core;

public class ContaPoupanca extends ContaBancaria{
    private double taxaRendimento;

    public ContaPoupanca(String nmrConta, String agencia, String nomeTitular, double taxaRendimento) {
        super(nmrConta, agencia, nomeTitular);
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public double calcularTributos(){
        return 0.0;
    }

    public void renderJuros() {
        if (this.saldo > 0) {
            double juros = this.saldo * this.taxaRendimento;
            this.depositar(juros, "RENDIMENTO DE JUROS");
        }
    }

}
