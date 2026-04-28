package br.edu.ufersa.minhacasaclandestinatech.model.entities;

import java.util.ArrayList;
import java.util.List;

public class Equipamento {

    // Atributos
    private int idEquipamento;
    private String nome;
    private int numeroSerie;
    private float preco;
    private int quantidadeEstoque;
    private List<Compra> compras;

    // Construtores
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

    // Métodos
    public void cadastrarEquipamento(Compra compra, int id) {
        this.idEquipamento = id;
        this.compras.add(compra);
        System.out.printf("Equipamento '%s' cadastrado com sucesso. ID: %d%n", nome, id);
    }

    public void editarEquipamento(Compra compra) {
        System.out.printf("Equipamento '%s' atualizado a partir da compra ID: %d%n",
                nome, compra.getIdCompra());
    }

    public void atualizarEstoque(Compra compra, int quant) {
        this.quantidadeEstoque += quant;
        System.out.printf("Estoque do equipamento '%s' atualizado para %d unidades.%n",
                nome, quantidadeEstoque);
    }

    public void consultarEquipamento(Compra compra) {
        System.out.printf("""
                === Consulta de Equipamento ===
                ID           : %d
                Nome         : %s
                Nº de Série  : %d
                Preço        : R$ %.2f
                Estoque      : %d unidades
                Compra ref.  : %d
                ================================%n""",
                idEquipamento, nome, numeroSerie, preco,
                quantidadeEstoque, compra.getIdCompra());
    }

    public void excluirEquipamento(Compra compra) {
        boolean removido = compras.remove(compra);
        if (removido) {
            System.out.printf("Compra ID %d removida do equipamento '%s'.%n",
                    compra.getIdCompra(), nome);
        } else {
            System.out.printf("Compra ID %d não encontrada no equipamento '%s'.%n",
                    compra.getIdCompra(), nome);
        }
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

}