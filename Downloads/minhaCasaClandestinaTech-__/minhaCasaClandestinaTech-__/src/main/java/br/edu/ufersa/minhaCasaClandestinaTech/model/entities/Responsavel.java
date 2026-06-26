package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Responsavel extends Pessoa {

    private int idResponsavel;
    private String telefone;

    public Responsavel(String nome, String endereco, String telefone) {
        super(nome, endereco);
        setTelefone(telefone);
    }

    public int getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(int idResponsavel) {
        if (idResponsavel <= 0)
            throw new IllegalArgumentException("ID inválido.");

        this.idResponsavel = idResponsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null &&
                telefone.contains("(") &&
                telefone.contains(")") &&
                telefone.contains("-")) {

            this.telefone = telefone;
        } else {
            throw new IllegalArgumentException(
                    "Telefone inválido! Ex: (84) 99999-9999");
        }
    }

    @Override
    public void excluir() {
        idResponsavel = 0;
        nome = null;
        endereco = null;
        telefone = null;

        System.out.println("Responsável excluído com sucesso!");
    }
}