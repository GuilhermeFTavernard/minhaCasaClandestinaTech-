package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Cliente {

    // Atributos
    private String nome;
    private String endereco;
    private int cpf;

    // Construtores
    public Cliente() {}

    public Cliente(String nome, String endereco, int cpf) {
        this.nome     = nome;
        this.endereco = endereco;
        this.cpf      = cpf;
    }

    // Métodos
    public void cadastrarCliente(String nome, String endereco, int cpf) {
        this.nome     = nome;
        this.endereco = endereco;
        this.cpf      = cpf;
        System.out.printf("Cliente cadastrado com sucesso: %s | CPF: %d%n", nome, cpf);
    }

    public void consultarCliente(String nome, String endereco, int cpf) {
        System.out.printf("""
                === Consulta de Cliente ===
                Nome     : %s
                Endereço : %s
                CPF      : %d
                ===========================%n""",
                nome, endereco, cpf);
    }

    public void excluirCliente(String nome, String endereco, int cpf) {
        System.out.printf("Cliente '%s' (CPF: %d) removido com sucesso.%n", nome, cpf);
        this.nome     = null;
        this.endereco = null;
        this.cpf      = 0;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Nome inválido.");
        this.nome = nome;
    }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) throw new IllegalArgumentException("Endereço inválido.");
        this.endereco = endereco;
    }

    public int getCpf() { return cpf; }
    public void setCpf(int cpf) {
        if (cpf <= 0) throw new IllegalArgumentException("CPF inválido.");
        this.cpf = cpf;
    }

}