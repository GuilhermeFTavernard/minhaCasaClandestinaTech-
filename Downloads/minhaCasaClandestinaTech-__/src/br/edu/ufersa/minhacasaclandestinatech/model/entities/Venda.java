package br.edu.ufersa.minhacasaclandestinatech.model.entities;

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
    private List<ItemVenda> itensVenda; // Renomeado para maior clareza

    // Construtor
    public Venda(int idVenda, Cliente cliente) {
        this.idVenda = idVenda;
        this.cliente = cliente;
        this.dataVenda = LocalDate.now();
        this.valorTotal = BigDecimal.ZERO; // Inicialização segura
        this.itensVenda = new ArrayList<>();
    }

    // Metodos
    public void adicionarItemDeVenda(ItemVenda item) {
        if (item != null) {
            this.itensVenda.add(item);
            // Atualiza o total somando o subtotal do novo item, evitando recalcular toda a lista (O(1) vs O(n))
            this.valorTotal = this.valorTotal.add(item.calcularSubtotal());
        }
    }

    public void removerItemDeVenda(ItemVenda item) {
        if (this.itensVenda.remove(item)) {
            // Subtrai o valor caso o item seja removido
            this.valorTotal = this.valorTotal.subtract(item.calcularSubtotal());
        }
    }

    // Getters
    public BigDecimal getValorTotal() { return valorTotal; }
    public LocalDate getDataVenda() { return dataVenda; }
    public List<ItemVenda> getItensVenda() {
        return Collections.unmodifiableList(itensVenda);
    }
}
