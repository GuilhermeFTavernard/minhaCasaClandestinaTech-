package com.minhacasa.estoque.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Responsavel = dono da casa onde os equipamentos ficam guardados. */
public class Responsavel {

    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty endereco = new SimpleStringProperty();
    private final StringProperty telefone = new SimpleStringProperty();

    public Responsavel(String nome, String endereco, String telefone) {
        this.nome.set(nome);
        this.endereco.set(endereco);
        this.telefone.set(telefone);
    }

    public String getNome() { return nome.get(); }
    public void setNome(String v) { nome.set(v); }
    public StringProperty nomeProperty() { return nome; }

    public String getEndereco() { return endereco.get(); }
    public void setEndereco(String v) { endereco.set(v); }
    public StringProperty enderecoProperty() { return endereco; }

    public String getTelefone() { return telefone.get(); }
    public void setTelefone(String v) { telefone.set(v); }
    public StringProperty telefoneProperty() { return telefone; }

    @Override
    public String toString() { return getNome(); }
}
