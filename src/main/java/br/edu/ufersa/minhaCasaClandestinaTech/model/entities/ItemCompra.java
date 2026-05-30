package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class ItemCompra {

    // Atributos
    private int idItemCompra;
    private int quantidade;
    private float precoUnitario;
    private String categoria;
    private int idCompra;

    // Construtor
    public ItemCompra(int idItemCompra, int idCompra, int quantidade, float precoUnitario, String categoria) {
        this.idItemCompra = idItemCompra;
        this.idCompra = idCompra;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
    }

    public int getIdItemCompra() { return idItemCompra; }
    public void setIdItemCompra(int idItemCompra) {
        if (idItemCompra > 0) this.idItemCompra = idItemCompra;
        else throw new RuntimeException("Id inválido!");
    }

    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) {
        if (idCompra > 0) this.idCompra = idCompra;
        else throw new RuntimeException("Id da compra inválido!");
    }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) {
        if (quantidade > 0) this.quantidade = quantidade;
        else throw new RuntimeException("Digite a quantidade");
    }

    public float getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(float precoUnitario) {
        if (precoUnitario > 0) this.precoUnitario = precoUnitario;
        else throw new RuntimeException("Digite o preço unitário");
    }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) {
        if (categoria != null) this.categoria = categoria;
        else throw new RuntimeException("Digite a categoria");
    }
    // Método útil (extra): calcular total do item
    public float calcularTotal() {
        return quantidade * precoUnitario;
    }
}
