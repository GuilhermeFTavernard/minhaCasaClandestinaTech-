package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public class Local {

    private int idLocal;
    private String nomeCasa;
    private String nomeCompartimento;
    private Responsavel responsavel;

    // Construtor
    public Local(String nomeCasa, String nomeCompartimento){
        setNomeCasa(nomeCasa);
        setNomeCompartimento(nomeCompartimento);
    }

    // Getters

    public int getIdLocal() { return idLocal; }

    public String getNomeCasa() {
        return nomeCasa;
    }

    public String getNomeCompartimento() {
        return nomeCompartimento;
    }

    public Responsavel getResponsavel() { return responsavel; }

    // Setters

    public void setIdLocal(int idLocal) {
        if (idLocal > 0) this.idLocal = idLocal;
        else throw new RuntimeException("Id inválido!");
    }

    public void setNomeCasa(String nomeCasa) throws RuntimeException{
        if (nomeCasa != null ){
            this.nomeCasa = nomeCasa;
        } else {
            throw new RuntimeException("Nome da casa inválido!");
        }
    }

    public void setNomeCompartimento(String nomeCompartimento) throws RuntimeException{
        if (nomeCompartimento != null ){
            this.nomeCompartimento = nomeCompartimento;
        } else {
            throw new RuntimeException("Informe um compartimento válido!");
        }
    }

    public void setResponsavel(Responsavel responsavel) { this.responsavel = responsavel; }

    //método

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