package br.edu.ufersa.minhacasaclandestinatech.model.entities;

public class Responsavel {

    private String nome;
    private String endereco;
    private String telefone;

    // Construtor
    public Responsavel(String nome, String endereco, String telefone){
        setNome(nome);
        setEndereco(endereco);
        setTelefone(telefone);
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    // Setters
    public void setNome(String nome) {
        if(nome != null) {
            this.nome = nome;
        } else {
            System.out.println("Nome inválido!");
        }
    }

    public void setEndereco(String endereco) {
        if (endereco != null){
            this.endereco = endereco;
        } else {
            System.out.println("Endereço inválido!");
        }
    }

    public void setTelefone(String telefone) {
        if (telefone != null && telefone.contains("(") 
            && telefone.contains(")") && telefone.contains("-")) {
            this.telefone = telefone;
        } else {
            System.out.println("Telefone inválido! Ex: (84) 99999-9999");
        }
    }

    // Editar
    public void editarResponsavel(String novoNome, String novoEndereco, String novoTelefone){
        setNome(novoNome);
        setEndereco(novoEndereco);
        setTelefone(novoTelefone);
        System.out.println("Responsável atualizado com sucesso!");
    }

    // Excluir
    public void excluirResponsavel(){
        this.nome = null;
        this.endereco = null;
        this.telefone = null;
        System.out.println("Responsável excluído com sucesso!");
    }
}