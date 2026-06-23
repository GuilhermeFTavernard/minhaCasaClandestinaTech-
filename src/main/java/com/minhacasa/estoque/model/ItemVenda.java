package com.minhacasa.estoque.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/** Um item (equipamento + quantidade) dentro de uma Venda. */
public class ItemVenda {

    private final ObjectProperty<Equipamento> equipamento = new SimpleObjectProperty<>();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final DoubleProperty precoUnitario = new SimpleDoubleProperty();

    public ItemVenda(Equipamento equipamento, int quantidade, double precoUnitario) {
        this.equipamento.set(equipamento);
        this.quantidade.set(quantidade);
        this.precoUnitario.set(precoUnitario);
    }

    public Equipamento getEquipamento() { return equipamento.get(); }
    public void setEquipamento(Equipamento v) { equipamento.set(v); }
    public ObjectProperty<Equipamento> equipamentoProperty() { return equipamento; }

    public int getQuantidade() { return quantidade.get(); }
    public void setQuantidade(int v) { quantidade.set(v); }
    public IntegerProperty quantidadeProperty() { return quantidade; }

    public double getPrecoUnitario() { return precoUnitario.get(); }
    public void setPrecoUnitario(double v) { precoUnitario.set(v); }
    public DoubleProperty precoUnitarioProperty() { return precoUnitario; }

    public double getSubtotal() { return getQuantidade() * getPrecoUnitario(); }
}
