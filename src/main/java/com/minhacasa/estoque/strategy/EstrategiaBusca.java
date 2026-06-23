package com.minhacasa.estoque.strategy;

import com.minhacasa.estoque.model.Equipamento;

/**
 * Strategy — interface funcional que define o contrato de filtragem.
 * Cada implementação representa um critério de busca distinto.
 */
@FunctionalInterface
public interface EstrategiaBusca {
    boolean corresponde(Equipamento equipamento, String termo);
}
