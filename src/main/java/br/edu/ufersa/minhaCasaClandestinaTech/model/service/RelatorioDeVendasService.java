package br.edu.ufersa.minhaCasaClandestinaTech.model.service;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.RelatorioDeVendasDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.RelatorioDeVendas;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class RelatorioDeVendasService {

    private RelatorioDeVendasDAO relatorioDAO = new RelatorioDeVendasDAO();

    public List<Venda> gerarRelatorio(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new RuntimeException("Datas de início e fim obrigatórias!");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new RuntimeException("Data de início não pode ser maior que a data de fim!");
        }

        List<Venda> vendas = relatorioDAO.buscarVendasComItens(
                dataInicio.toString(),
                dataFim.toString()
        );

        RelatorioDeVendas relatorio = new RelatorioDeVendas(vendas);
        return relatorio.gerarRelatorioVendas(dataInicio, dataFim);
    }

    public BigDecimal calcularReceitaTotal(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new RuntimeException("Datas de início e fim obrigatórias!");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new RuntimeException("Data de início não pode ser maior que a data de fim!");
        }

        List<Venda> vendas = relatorioDAO.buscarVendasPorPeriodo(
                dataInicio.toString(),
                dataFim.toString()
        );

        RelatorioDeVendas relatorio = new RelatorioDeVendas(vendas);
        return relatorio.calcularReceitaTotalPeriodo(vendas);
    }

    public int calcularQuantidadeProdutos(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new RuntimeException("Datas de início e fim obrigatórias!");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new RuntimeException("Data de início não pode ser maior que a data de fim!");
        }

        List<Venda> vendas = relatorioDAO.buscarVendasComItens(
                dataInicio.toString(),
                dataFim.toString()
        );

        RelatorioDeVendas relatorio = new RelatorioDeVendas(vendas);
        return relatorio.calcularQuantidadeTotalProdutos(vendas);
    }

    public List<Venda> listar() {
        return relatorioDAO.listar();
    }

    public ResultSet buscar(String param) {
        if (param == null || param.isBlank()) {
            throw new RuntimeException("Parâmetro obrigatório para busca!");
        }
        return relatorioDAO.buscar(param);
    }
}
