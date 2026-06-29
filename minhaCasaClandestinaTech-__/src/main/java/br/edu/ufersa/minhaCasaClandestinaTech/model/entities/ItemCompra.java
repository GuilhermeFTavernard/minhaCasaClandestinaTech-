package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class ItemCompra {

    private int idItemCompra;
    private int quantidade;
    private float precoUnitario;
    private String categoria;
    private int idCompra;
    private Equipamento equipamento;

    public ItemCompra(int idItemCompra, int idCompra, int quantidade, float precoUnitario, String categoria) {
        this.idItemCompra = idItemCompra;
        this.idCompra = idCompra;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
    }

    public ItemCompra(int idItemCompra, int idCompra, int quantidade, float precoUnitario,
                      String categoria, Equipamento equipamento) {
        this(idItemCompra, idCompra, quantidade, precoUnitario, categoria);
        this.equipamento = equipamento;
    }

    public ItemCompra(int quantidade, float precoUnitario, String categoria, Equipamento equipamento) {
        this(0, 0, quantidade, precoUnitario, categoria, equipamento);
    }

    public int getIdItemCompra() {
        return idItemCompra;
    }

    public void setIdItemCompra(int idItemCompra) {
        if (idItemCompra > 0) {
            this.idItemCompra = idItemCompra;
        } else {
            throw new RuntimeException("Id inválido!");
        }
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        if (idCompra > 0) {
            this.idCompra = idCompra;
        } else {
            throw new RuntimeException("Id da compra inválido!");
        }
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if (quantidade > 0) {
            this.quantidade = quantidade;
        } else {
            throw new RuntimeException("Digite a quantidade");
        }
    }

    public float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(float precoUnitario) {
        if (precoUnitario > 0) {
            this.precoUnitario = precoUnitario;
        } else {
            throw new RuntimeException("Digite o preço unitário");
        }
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria != null) {
            this.categoria = categoria;
        } else {
            throw new RuntimeException("Digite a categoria");
        }
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public float calcularTotal() {
        return quantidade * precoUnitario;
    }
}
