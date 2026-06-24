package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Usuario {

    private int idUsuario;
    private String email;
    private String senha;

    // Construtor
    public Usuario(int idUsuario, String email, String senha){
        this.idUsuario = idUsuario;
        this.email = email;
        this.senha = senha;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) throws RuntimeException{
        if(idUsuario >= 0){
            this.idUsuario = idUsuario;
        } else {
            throw new RuntimeException("O número do Id tem que ser positivo!");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws RuntimeException{
        if(email != null && email.contains("@") && email.contains(".")){
            this.email = email;
        } else {
            throw new RuntimeException("Digite algum email!");
        }
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) throws RuntimeException{
        if(senha != null) {
            this.senha = senha;
        } else {
            throw new RuntimeException("Alguma senha!");
        }
    }




    //Método Logar
    public void logar(String email, String senha){
        if(this.email.equals(email) && this.senha.equals(senha)){
            System.out.println("Login realizado com sucesso!");
        } else {
            System.out.println("Email ou senha inválidos!");
        }
    }
}