package br.edu.ufersa.minhaCasaClandestinaTech.model.strategy;

public final class EstrategiasBusca {

    private EstrategiasBusca() {}

    public static final EstrategiaBusca POR_NOME = (eq, termo) ->
            eq.getNome() != null
                    && eq.getNome().toLowerCase().contains(termo);

    // getNumeroSerie() é int na main — precisa de String.valueOf()
    public static final EstrategiaBusca POR_SERIE = (eq, termo) ->
            String.valueOf(eq.getNumeroSerie()).contains(termo);

    public static final EstrategiaBusca POR_RESPONSAVEL = (eq, termo) ->
            eq.getResponsavel() != null
                    && eq.getResponsavel().getNome() != null
                    && eq.getResponsavel().getNome().toLowerCase().contains(termo);

    // getDescricao() foi adicionado no Passo 1
    public static final EstrategiaBusca POR_LOCAL = (eq, termo) ->
            eq.getLocal() != null
                    && eq.getLocal().getDescricao().toLowerCase().contains(termo);
}