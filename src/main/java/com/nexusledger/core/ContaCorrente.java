package com.nexusledger.core;

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
        // SOBRESCRITA DE REGRA DE NEGÓCIO: Na Conta Corrente, o cliente pode usar o Cheque Especial.
        // Portanto, o limite de saque passa a ser saldo atual SOMADO ao limite de crédito liberado pelo banco.
        else if(valor > this.saldo + this.limiteChequeES){
            throw new IllegalArgumentException("Saldo Insuficiente.");
        } else {
            this.saldo -= valor;
        }
    }
}
