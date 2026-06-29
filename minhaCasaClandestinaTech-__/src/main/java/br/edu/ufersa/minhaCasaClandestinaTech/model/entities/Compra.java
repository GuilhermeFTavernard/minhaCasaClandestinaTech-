package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private int idCompra;
    private LocalDate dataCompra;
    private String fornecedor;
    private List<ItemCompra> listaDeItens;

    public Compra() {
        this.listaDeItens = new ArrayList<>();
    }

    public Compra(int idCompra, LocalDate dataCompra, String fornecedor) {
        this.idCompra = idCompra;
        this.dataCompra = dataCompra;
        this.fornecedor = fornecedor;
        this.listaDeItens = new ArrayList<>();
    }

    public Compra(LocalDate dataCompra, String fornecedor) {
        this(0, dataCompra, fornecedor);
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        if (idCompra <= 0) {
            throw new RuntimeException("Id da compra inválido.");
        }
        this.idCompra = idCompra;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        if (dataCompra == null) {
            throw new RuntimeException("Digite uma data.");
        }
        this.dataCompra = dataCompra;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        if (fornecedor == null || fornecedor.isBlank()) {
            throw new RuntimeException("Digite o fornecedor.");
        }
        this.fornecedor = fornecedor;
    }

    public List<ItemCompra> getListaDeItens() {
        return listaDeItens;
    }

    public void setListaDeItens(List<ItemCompra> listaDeItens) {
        if (listaDeItens == null) {
            this.listaDeItens = new ArrayList<>();
        } else {
            this.listaDeItens = listaDeItens;
        }
    }

    public void adicionarItemDeCompra(ItemCompra item) {
        if (item == null) {
            throw new RuntimeException("Item de compra inválido.");
        }
        listaDeItens.add(item);
    }

    public void removerItemDeCompra(ItemCompra item) {
        listaDeItens.remove(item);
    }

    public void editarCompra(int index, ItemCompra novoItem) {
        if (index < 0 || index >= listaDeItens.size()) {
            throw new RuntimeException("Índice inválido.");
        }
        listaDeItens.set(index, novoItem);
    }

    public void excluirCompra() {
        listaDeItens.clear();
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemCompra item : listaDeItens) {
            total += item.calcularTotal();
        }
        return total;
    }
}
