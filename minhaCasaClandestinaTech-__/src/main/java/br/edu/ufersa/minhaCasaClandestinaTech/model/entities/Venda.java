package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Venda {

    // Atributos
    private int idVenda;
    private LocalDate dataVenda;
    private BigDecimal valorTotal;
    private Cliente cliente;
    private List<ItemVenda> itensVenda;

    // Construtor
    public Venda(int idVenda, Cliente cliente) {
        this.idVenda = idVenda;
        this.cliente = cliente;
        this.dataVenda = LocalDate.now();
        this.valorTotal = BigDecimal.ZERO;
        this.itensVenda = new ArrayList<>();
    }

    // Metodos
    public void adicionarItemDeVenda(ItemVenda item) {
        if (item != null) {
            this.itensVenda.add(item);
            this.valorTotal = this.valorTotal.add(item.calcularSubtotal());
        }
    }

    public void removerItemDeVenda(ItemVenda item) {
        if (this.itensVenda.remove(item)) {
            this.valorTotal = this.valorTotal.subtract(item.calcularSubtotal());
        }
    }

    // Getters
    public int getIdVenda() { return idVenda; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDate getDataVenda() { return dataVenda; }
    public Cliente getCliente() { return cliente; }
    public List<ItemVenda> getItensVenda() {
        return Collections.unmodifiableList(itensVenda);
    }

    // Setters
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}
