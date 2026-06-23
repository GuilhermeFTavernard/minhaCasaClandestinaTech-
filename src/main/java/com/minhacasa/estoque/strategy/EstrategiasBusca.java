package com.minhacasa.estoque.strategy;

/**
 * Catálogo de estratégias de busca prontas para uso.
 *
 * Como EstrategiaBusca é uma @FunctionalInterface, cada estratégia
 * é definida como uma lambda — sem precisar criar uma classe separada
 * para cada critério.
 *
 * Uso:
 *   EstrategiaBusca estrategia = EstrategiasBusca.POR_NOME;
 *   filteredList.setPredicate(eq -> estrategia.corresponde(eq, termo));
 */
public final class EstrategiasBusca {

    private EstrategiasBusca() {}

    /** Busca pelo nome do equipamento (case-insensitive, busca parcial). */
    public static final EstrategiaBusca POR_NOME = (eq, termo) ->
            eq.getNome() != null
            && eq.getNome().toLowerCase().contains(termo);

    /** Busca pelo número de série (case-insensitive, busca parcial). */
    public static final EstrategiaBusca POR_SERIE = (eq, termo) ->
            eq.getNumeroSerie() != null
            && eq.getNumeroSerie().toLowerCase().contains(termo);

    /** Busca pelo nome do responsável (case-insensitive, busca parcial). */
    public static final EstrategiaBusca POR_RESPONSAVEL = (eq, termo) ->
            eq.getResponsavel() != null
            && eq.getResponsavel().getNome() != null
            && eq.getResponsavel().getNome().toLowerCase().contains(termo);

    /**
     * Busca pela descrição do local (casa + compartimento).
     * Usa Local.getDescricao(), ex: "Casa do Toinho / Quarto da mãe".
     */
    public static final EstrategiaBusca POR_LOCAL = (eq, termo) ->
            eq.getLocal() != null
            && eq.getLocal().getDescricao() != null
            && eq.getLocal().getDescricao().toLowerCase().contains(termo);
}
