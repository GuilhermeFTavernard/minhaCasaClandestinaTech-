package com.minhacasa.estoque.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cliente {

    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty cpf = new SimpleStringProperty();
    private final StringProperty endereco = new SimpleStringProperty();

    public Cliente(String nome, String cpf, String endereco) {
        this.nome.set(nome);
        this.cpf.set(cpf);
        this.endereco.set(endereco);
    }

    public String getNome() { return nome.get(); }
    public void setNome(String v) { nome.set(v); }
    public StringProperty nomeProperty() { return nome; }

    public String getCpf() { return cpf.get(); }
    public void setCpf(String v) { cpf.set(v); }
    public StringProperty cpfProperty() { return cpf; }

    public String getEndereco() { return endereco.get(); }
    public void setEndereco(String v) { endereco.set(v); }
    public StringProperty enderecoProperty() { return endereco; }

    @Override
    public String toString() { return getNome(); }
}
