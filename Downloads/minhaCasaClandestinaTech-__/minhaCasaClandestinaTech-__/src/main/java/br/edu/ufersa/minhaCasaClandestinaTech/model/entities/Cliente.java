package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Cliente {

    // Atributos
    private int idCliente;
    private String nome;
    private String endereco;
    private String cpf;

    // Construtores
    public Cliente() {
    }

    public Cliente(String nome, String endereco, String cpf) {
        this.nome = nome;
        this.endereco = endereco;
        this.cpf = cpf;
    }

    // Getters e Setters
    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        if (idCliente < 0) {
            throw new IllegalArgumentException("ID do cliente inválido.");
        }
        this.idCliente = idCliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("Endereço inválido.");
        }
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF inválido.");
        }
        this.cpf = cpf;
    }

    // Métodos antigos mantidos, caso alguma parte do projeto ainda use
    public void cadastrarCliente(String nome, String endereco, String cpf) {
        setNome(nome);
        setEndereco(endereco);
        setCpf(cpf);
        System.out.printf("Cliente cadastrado com sucesso: %s | CPF: %s%n", nome, cpf);
    }

    public void consultarCliente(String nome, String endereco, String cpf) {
        System.out.printf("""
                === Consulta de Cliente ===
                Nome     : %s
                Endereço : %s
                CPF      : %s
                ===========================%n""",
                nome, endereco, cpf);
    }

    public void excluirCliente(String nome, String endereco, String cpf) {
        System.out.printf("Cliente '%s' (CPF: %s) removido com sucesso.%n", nome, cpf);
        this.idCliente = 0;
        this.nome = null;
        this.endereco = null;
        this.cpf = null;
    }
}
