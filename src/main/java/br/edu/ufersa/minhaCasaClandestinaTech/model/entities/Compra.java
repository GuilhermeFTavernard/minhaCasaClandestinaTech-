package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    // Atributos
    private int idCompra;
    private LocalDate dataCompra;
    private String fornecedor;
    private List<ItemCompra> listaDeItens;

    // Construtor
    public Compra(int idCompra, LocalDate dataCompra, String fornecedor) {
        this.idCompra = idCompra;
        this.dataCompra = dataCompra;
        this.fornecedor = fornecedor;
        this.listaDeItens = new ArrayList<>();
    }

    // Getters e Setters
    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) throws RuntimeException{
        if(idCompra > 0){
            this.idCompra = idCompra;
        }else{
            throw new RuntimeException("Digite o idCompra");
        }
        
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) throws RuntimeException{
        if(dataCompra != null){
            this.dataCompra = dataCompra;
        }else{
            throw new RuntimeException("Digite uma data");
        }
        
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) throws RuntimeException{
        if(fornecedor != null){
            this.fornecedor = fornecedor;
        }else{
            throw new RuntimeException("Digite o fornecedor");
        }
        
    }

    public List<ItemCompra> getListaDeItens() {
        return listaDeItens;
    }

    // Métodos

    public void adicionarItemDeCompra(ItemCompra item) {
        listaDeItens.add(item);
        System.out.println("Item adicionado com sucesso!");
    }

    public void removerItemDeCompra(ItemCompra item) {
        listaDeItens.remove(item);
        System.out.println("Item removido com sucesso!");
    }

    public void editarCompra(int index, ItemCompra novoItem) {
        if (index >= 0 && index < listaDeItens.size()) {
            listaDeItens.set(index, novoItem);
            System.out.println("Item editado com sucesso!");
        } else {
            System.out.println("Índice inválido!");
        }
    }

    public void excluirCompra() {
        listaDeItens.clear();
        System.out.println("Compra excluída com sucesso!");
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemCompra item : listaDeItens) {
            total += item.calcularTotal();
        }
        return total;
    }
}