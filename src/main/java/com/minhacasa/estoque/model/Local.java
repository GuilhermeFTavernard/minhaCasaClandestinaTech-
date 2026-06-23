package com.minhacasa.estoque.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Local de armazenagem: uma casa + um comodo/compartimento dentro dela. */
public class Local {

    private final StringProperty casa = new SimpleStringProperty();
    private final StringProperty compartimento = new SimpleStringProperty();
    private final ObjectProperty<Responsavel> responsavel = new SimpleObjectProperty<>();

    public Local(String casa, String compartimento, Responsavel responsavel) {
        this.casa.set(casa);
        this.compartimento.set(compartimento);
        this.responsavel.set(responsavel);
    }

    public String getCasa() { return casa.get(); }
    public void setCasa(String v) { casa.set(v); }
    public StringProperty casaProperty() { return casa; }

    public String getCompartimento() { return compartimento.get(); }
    public void setCompartimento(String v) { compartimento.set(v); }
    public StringProperty compartimentoProperty() { return compartimento; }

    public Responsavel getResponsavel() { return responsavel.get(); }
    public void setResponsavel(Responsavel v) { responsavel.set(v); }
    public ObjectProperty<Responsavel> responsavelProperty() { return responsavel; }

    /** Ex.: "Casa do Toinho / Quarto da mae" */
    public String getDescricao() {
        return getCasa() + " / " + getCompartimento();
    }

    @Override
    public String toString() { return getDescricao(); }
}
