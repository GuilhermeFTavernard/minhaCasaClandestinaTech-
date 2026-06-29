package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.time.LocalDate;

public class MovimentacaoEstoque {

    public enum TipoMovimentacao {
        ENTRADA, SAIDA, AJUSTE
    }

    private int idMovimentacao;
    private LocalDate data;
    private int quantidade;
    private Equipamento equipamento;
    private TipoMovimentacao tipo;

    public MovimentacaoEstoque() {
        this.data = LocalDate.now();
    }

    public MovimentacaoEstoque(Equipamento equipamento, int quantidade) {
        this.data = LocalDate.now();
        this.equipamento = equipamento;
        this.quantidade = quantidade;
    }

    public void entrada() {
        this.tipo = TipoMovimentacao.ENTRADA;
        equipamento.setQuantidadeEstoque(
                equipamento.getQuantidadeEstoque() + quantidade
        );
    }

    public void saida() {
        this.tipo = TipoMovimentacao.SAIDA;
        equipamento.setQuantidadeEstoque(
                equipamento.getQuantidadeEstoque() - quantidade
        );
    }

    public void ajuste(int novaQuantidade) {
        this.tipo = TipoMovimentacao.AJUSTE;
        this.quantidade = novaQuantidade;
        equipamento.setQuantidadeEstoque(novaQuantidade);
    }

    public int getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(int idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimentacao tipo) {
        this.tipo = tipo;
    }
}
