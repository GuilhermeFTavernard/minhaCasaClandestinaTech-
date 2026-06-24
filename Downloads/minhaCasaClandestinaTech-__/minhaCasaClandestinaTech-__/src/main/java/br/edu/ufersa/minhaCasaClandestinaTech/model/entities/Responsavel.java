package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Responsavel {

    private int idResponsavel;
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

    public int getIdResponsavel() { return idResponsavel; }

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

    public void setIdResponsavel(int idResponsavel) {
        if (idResponsavel > 0) this.idResponsavel = idResponsavel;
        else throw new RuntimeException("Id inválido!");
    }

    public void setNome(String nome) throws RuntimeException{
        if(nome != null) {
            this.nome = nome;
        } else {
            throw new RuntimeException("Nome inválido!");
        }
    }

    public void setEndereco(String endereco) throws RuntimeException{
        if (endereco != null){
            this.endereco = endereco;
        } else {
            throw new RuntimeException("Endereço inválido!");
        }
    }

    public void setTelefone(String telefone) throws RuntimeException{
        if (telefone != null && telefone.contains("(") 
            && telefone.contains(")") && telefone.contains("-")) {
            this.telefone = telefone;
        } else {
            throw new RuntimeException("Telefone inválido! Ex: (84) 99999-9999");
        }
    }

    //método

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