package br.edu.ufersa.minhacasaclandestinatech.model.entities;

public class ItemCompra {

    // Atributos
    private int quantidade;
    private float precoUnitario;
    private String categoria;

    // Construtor
    public ItemCompra(int quantidade, float precoUnitario, String categoria) {
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        if(quantidade > 0){
            this.quantidade = quantidade;
        }else{
            System.out.println("Digite a quantidade");
        }
    }

    public float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(float precoUnitario) {
        if(precoUnitario > 0){
            this.precoUnitario = precoUnitario;
        }else{
            System.out.println("Digite o Preço unitário");
        }
        
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if(categoria != null){
            this.categoria = categoria;
        }else{
            System.out.println("Digite a categoria");
        }
        
    }

    // Método útil (extra): calcular total do item
    public float calcularTotal() {
        return quantidade * precoUnitario;
    }
}