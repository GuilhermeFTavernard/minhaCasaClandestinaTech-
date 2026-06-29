package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public abstract class Pessoa {

    protected String nome;
    protected String endereco;

    public Pessoa() {
    }

    public Pessoa(String nome, String endereco) {
        setNome(nome);
        setEndereco(endereco);
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    // Setters
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("Endereço inválido.");
        }
        this.endereco = endereco;
    }

    // Método comum
    public void editar(String nome, String endereco) {
        setNome(nome);
        setEndereco(endereco);
    }

    // Cada classe implementa sua exclusão
    public abstract void excluir();
}