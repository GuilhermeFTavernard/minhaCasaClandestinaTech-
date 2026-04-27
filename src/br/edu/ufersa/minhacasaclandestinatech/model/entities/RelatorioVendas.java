package br.edu.ufersa.minhacasaclandestinatech.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioDeVendas {
    private List<Venda> baseDeVendas;

    public RelatorioDeVendas(List<Venda> baseDeVendas) {
        this.baseDeVendas = baseDeVendas;
    }

    /**
     * Filtra vendas utilizando a moderna e otimizada Java Stream API.
     */
    public List<Venda> gerarRelatorioVendas(LocalDate inicio, LocalDate fim) {
        return baseDeVendas.stream()
                .filter(v -> !v.getDataVenda().isBefore(inicio) && !v.getDataVenda().isAfter(fim))
                .collect(Collectors.toList());
    }

    /**
     * Calcula a receita total de uma lista filtrada somando os BigDecimals via map/reduce.
     */
    public BigDecimal calcularReceitaTotalPeriodo(List<Venda> vendasFiltradas) {
        return vendasFiltradas.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retorna a quantidade total de produtos (itens) vendidos no período.
     * Útil para o label "Quant. Produtos: 12" da sua tela.
     */
    public int calcularQuantidadeTotalProdutos(List<Venda> vendasFiltradas) {
        return vendasFiltradas.stream()
                .flatMap(venda -> venda.getItensVenda().stream())
                .mapToInt(ItemVenda::getQuantidade)
                .sum();
    }
}
