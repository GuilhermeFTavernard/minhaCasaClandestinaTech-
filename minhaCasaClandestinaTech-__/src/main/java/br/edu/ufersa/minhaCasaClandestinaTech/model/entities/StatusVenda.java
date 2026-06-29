package br.edu.ufersa.minhaCasaClandestinaTech.model.entities;

public enum StatusVenda {
    ATIVA("ativa"),
    CANCELADA("cancelada");

    private final String label;

    StatusVenda(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
