package com.minhacasa.estoque.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class Venda {

    private final StringProperty numero = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> data = new SimpleObjectProperty<>();
    private final ObjectProperty<Cliente> cliente = new SimpleObjectProperty<>();
    private final ObservableList<ItemVenda> itens = FXCollections.observableArrayList();
    private final ObjectProperty<StatusVenda> status = new SimpleObjectProperty<>();

    public Venda(String numero, LocalDate data, Cliente cliente, StatusVenda status) {
        this.numero.set(numero);
        this.data.set(data);
        this.cliente.set(cliente);
        this.status.set(status);
    }

    public String getNumero() { return numero.get(); }
    public void setNumero(String v) { numero.set(v); }
    public StringProperty numeroProperty() { return numero; }

    public LocalDate getData() { return data.get(); }
    public void setData(LocalDate v) { data.set(v); }
    public ObjectProperty<LocalDate> dataProperty() { return data; }

    public Cliente getCliente() { return cliente.get(); }
    public void setCliente(Cliente v) { cliente.set(v); }
    public ObjectProperty<Cliente> clienteProperty() { return cliente; }

    public ObservableList<ItemVenda> getItens() { return itens; }

    public StatusVenda getStatus() { return status.get(); }
    public void setStatus(StatusVenda v) { status.set(v); }
    public ObjectProperty<StatusVenda> statusProperty() { return status; }

    public int getQuantidadeItens() {
        return itens.stream().mapToInt(ItemVenda::getQuantidade).sum();
    }

    public double getTotal() {
        return itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
    }
}
