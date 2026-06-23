package com.minhacasa.estoque.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class Compra {

    private final ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private final ObjectProperty<Equipamento> equipamento = new SimpleObjectProperty<>();
    private final StringProperty fornecedor = new SimpleStringProperty();
    private final IntegerProperty quantidade = new SimpleIntegerProperty();
    private final DoubleProperty custoUnitario = new SimpleDoubleProperty();

    public Compra(LocalDate data, Equipamento equipamento, String fornecedor,
                  int quantidade, double custoUnitario) {
        this.data.set(data);
        this.equipamento.set(equipamento);
        this.fornecedor.set(fornecedor);
        this.quantidade.set(quantidade);
        this.custoUnitario.set(custoUnitario);
    }

    public LocalDate getData() { return data.get(); }
    public void setData(LocalDate v) { data.set(v); }
    public ObjectProperty<LocalDate> dataProperty() { return data; }

    public Equipamento getEquipamento() { return equipamento.get(); }
    public void setEquipamento(Equipamento v) { equipamento.set(v); }
    public ObjectProperty<Equipamento> equipamentoProperty() { return equipamento; }

    public String getFornecedor() { return fornecedor.get(); }
    public void setFornecedor(String v) { fornecedor.set(v); }
    public StringProperty fornecedorProperty() { return fornecedor; }

    public int getQuantidade() { return quantidade.get(); }
    public void setQuantidade(int v) { quantidade.set(v); }
    public IntegerProperty quantidadeProperty() { return quantidade; }

    public double getCustoUnitario() { return custoUnitario.get(); }
    public void setCustoUnitario(double v) { custoUnitario.set(v); }
    public DoubleProperty custoUnitarioProperty() { return custoUnitario; }

    public double getTotal() { return getQuantidade() * getCustoUnitario(); }
}
