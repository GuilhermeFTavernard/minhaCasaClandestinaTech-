package br.edu.ufersa.minhaCasaClandestinaTech.model.strategy;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;

@FunctionalInterface
public interface EstrategiaBusca {
    boolean corresponde(Equipamento equipamento, String termo);
}