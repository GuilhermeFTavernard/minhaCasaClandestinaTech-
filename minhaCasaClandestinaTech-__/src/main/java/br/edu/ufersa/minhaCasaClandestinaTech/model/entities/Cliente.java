package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Cliente extends Pessoa {

    private int idCliente;
    private String cpf;

    public Cliente() {
        super();
    }

    public Cliente(String nome, String endereco, String cpf) {
        super(nome, endereco);
        setCpf(cpf);
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente < 0)
            throw new IllegalArgumentException("ID inválido.");
        this.idCliente = idCliente;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank())
            throw new IllegalArgumentException("CPF inválido.");
        this.cpf = cpf;
    }

    @Override
    public void excluir() {
        idCliente = 0;
        nome = null;
        endereco = null;
        cpf = null;

        System.out.println("Cliente excluído com sucesso!");
    }
}