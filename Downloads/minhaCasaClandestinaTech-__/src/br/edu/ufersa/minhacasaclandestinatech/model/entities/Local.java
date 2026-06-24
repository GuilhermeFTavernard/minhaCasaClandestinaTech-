package br.edu.ufersa.minhacasaclandestinatech.model.entities;

public class Local {

    private String nomeCasa;
    private String nomeCompartimento;

    // Construtor
    public Local(String nomeCasa, String nomeCompartimento){
        setNomeCasa(nomeCasa);
        setNomeCompartimento(nomeCompartimento);
    }

    // Getters
    public String getNomeCasa() {
        return nomeCasa;
    }

    public String getNomeCompartimento() {
        return nomeCompartimento;
    }

    // Setters
    public void setNomeCasa(String nomeCasa) {
        if (nomeCasa != null ){
            this.nomeCasa = nomeCasa;
        } else {
            System.out.println("Nome da casa inválido!");
        }
    }

    public void setNomeCompartimento(String nomeCompartimento) {
        if (nomeCompartimento != null ){
            this.nomeCompartimento = nomeCompartimento;
        } else {
            System.out.println("Informe um compartimento válido!");
        }
    }

    // Editar
    public void editarLocal(String novoNomeCasa, String novoNomeCompartimento){
        setNomeCasa(novoNomeCasa);
        setNomeCompartimento(novoNomeCompartimento);
        System.out.println("Local atualizado com sucesso!");
    }

    // Excluir
    public void excluirLocal(){
        this.nomeCasa = null;
        this.nomeCompartimento = null;
        System.out.println("Local excluído com sucesso!");
    }
}