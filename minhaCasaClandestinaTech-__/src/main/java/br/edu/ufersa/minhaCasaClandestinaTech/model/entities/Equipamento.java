package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Equipamento {

    private int idEquipamento;
    private String nome;
    private int numeroSerie;
    private float preco;
    private int quantidadeEstoque;
    private List<Compra> compras;
    private Local local;
    private Responsavel responsavel;

    public Equipamento() {
        this.compras = new ArrayList<>();
    }

    public Equipamento(int idEquipamento, String nome, int numeroSerie,
                       float preco, int quantidadeEstoque) {
        this.idEquipamento     = idEquipamento;
        this.nome              = nome;
        this.numeroSerie       = numeroSerie;
        this.preco             = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.compras           = new ArrayList<>();
    }

    public void cadastrarEquipamento(Compra compra, int id) {
        this.idEquipamento = id;
        this.compras.add(compra);
    }

    public void editarEquipamento(Compra compra) {}

    public void atualizarEstoque(Compra compra, int quant) {
        this.quantidadeEstoque += quant;
    }

    public void consultarEquipamento(Compra compra) {}

    public void excluirEquipamento(Compra compra) {
        compras.remove(compra);
    }

    // Getters e Setters
    public int getIdEquipamento() { return idEquipamento; }
    public void setIdEquipamento(int idEquipamento) { this.idEquipamento = idEquipamento; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(int numeroSerie) { this.numeroSerie = numeroSerie; }

    public float getPreco() { return preco; }
    public void setPreco(float preco) { this.preco = preco; }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public List<Compra> getCompras() { return List.copyOf(compras); }

    public Local getLocal() { return local; }
    public void setLocal(Local local) { this.local = local; }

    public Responsavel getResponsavel() { return responsavel; }
    public void setResponsavel(Responsavel responsavel) { this.responsavel = responsavel; }
}