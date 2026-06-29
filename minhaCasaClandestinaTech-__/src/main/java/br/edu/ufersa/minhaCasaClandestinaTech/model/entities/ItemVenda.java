package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.math.BigDecimal;

public class ItemVenda {

    // Atributos
    private int idItemVenda;
    private int idVenda;
    private int quantidade;
    private BigDecimal precoUnitario;
    private Equipamento equipamento;

    // Construtor
    public ItemVenda(int quantidade, BigDecimal precoUnitario, Equipamento equipamento) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.equipamento = equipamento;
    }

    // Calcula o subtotal da linha usando métodos do BigDecimal
    public BigDecimal calcularSubtotal() {
        return this.precoUnitario.multiply(BigDecimal.valueOf(this.quantidade));
    }

    // Getters
    public int getIdItemVenda() { return idItemVenda; }
    public int getIdVenda() { return idVenda; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public Equipamento getEquipamento() { return equipamento; }

    // Setters
    public void setIdItemVenda(int idItemVenda) { this.idItemVenda = idItemVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    public void setEquipamento(Equipamento equipamento) { this.equipamento = equipamento; }
}
