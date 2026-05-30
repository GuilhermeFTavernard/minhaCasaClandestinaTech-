package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.time.LocalDate;

public class MovimentacaoEstoque {

    // Enum interno para tipo de movimentação

    public enum TipoMovimentacao {
        ENTRADA, SAIDA, AJUSTE
    }

    // Atributos
    private LocalDate data;
    private int quantidade;
    private Equipamento equipamento;
    private TipoMovimentacao tipo;

    // Construtores
    public MovimentacaoEstoque() {
        this.data = LocalDate.now();
    }

    public MovimentacaoEstoque(Equipamento equipamento, int quantidade) {
        this.data        = LocalDate.now();
        this.equipamento = equipamento;
        this.quantidade  = quantidade;
    }

    // Métodos
    public void entrada() {
        this.tipo = TipoMovimentacao.ENTRADA;
        equipamento.setQuantidadeEstoque(
                equipamento.getQuantidadeEstoque() + quantidade
        );
        System.out.printf("[ENTRADA] %s | Equipamento: '%s' | Qtd: +%d | Novo estoque: %d%n",
                data, equipamento.getNome(), quantidade, equipamento.getQuantidadeEstoque());
    }

    public void saida() {
        this.tipo = TipoMovimentacao.SAIDA;
        equipamento.setQuantidadeEstoque(
                equipamento.getQuantidadeEstoque() - quantidade
        );
        System.out.printf("[SAÍDA]   %s | Equipamento: '%s' | Qtd: -%d | Novo estoque: %d%n",
                data, equipamento.getNome(), quantidade, equipamento.getQuantidadeEstoque());
    }

    public void ajuste(int novaQuantidade) {
        this.tipo      = TipoMovimentacao.AJUSTE;
        this.quantidade = novaQuantidade;
        int estoqueAnterior = equipamento.getQuantidadeEstoque();
        equipamento.setQuantidadeEstoque(novaQuantidade);
        System.out.printf("[AJUSTE]  %s | Equipamento: '%s' | Antes: %d | Depois: %d%n",
                data, equipamento.getNome(), estoqueAnterior, novaQuantidade);
    }

    // Getters e Setters
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Equipamento getEquipamento() { return equipamento; }
    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public TipoMovimentacao getTipo() { return tipo; }
}
