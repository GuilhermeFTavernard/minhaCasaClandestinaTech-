package com.minhacasa.estoque.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Equipamento {

    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty numeroSerie = new SimpleStringProperty();
    private final DoubleProperty preco = new SimpleDoubleProperty();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final ObjectProperty<Local> local = new SimpleObjectProperty<>();
    private final ObjectProperty<Responsavel> responsavel = new SimpleObjectProperty<>();

    public Equipamento(String nome, String numeroSerie, double preco, int quantidade,
                        Local local, Responsavel responsavel) {
        this.nome.set(nome);
        this.numeroSerie.set(numeroSerie);
        this.preco.set(preco);
        this.quantidade.set(quantidade);
        this.local.set(local);
        this.responsavel.set(responsavel);
    }

    public String getNome() { return nome.get(); }
    public void setNome(String v) { nome.set(v); }
    public StringProperty nomeProperty() { return nome; }

    public String getNumeroSerie() { return numeroSerie.get(); }
    public void setNumeroSerie(String v) { numeroSerie.set(v); }
    public StringProperty numeroSerieProperty() { return numeroSerie; }

    public double getPreco() { return preco.get(); }
    public void setPreco(double v) { preco.set(v); }
    public DoubleProperty precoProperty() { return preco; }

    public int getQuantidade() { return quantidade.get(); }
    public void setQuantidade(int v) { quantidade.set(v); }
    public IntegerProperty quantidadeProperty() { return quantidade; }

    public Local getLocal() { return local.get(); }
    public void setLocal(Local v) { local.set(v); }
    public ObjectProperty<Local> localProperty() { return local; }

    public Responsavel getResponsavel() { return responsavel.get(); }
    public void setResponsavel(Responsavel v) { responsavel.set(v); }
    public ObjectProperty<Responsavel> responsavelProperty() { return responsavel; }

    public double getValorTotal() { return getPreco() * getQuantidade(); }

    @Override
    public String toString() { return getNome(); }
}
