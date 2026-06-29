package br.edu.ufersa.minhaCasaClandestinaTech.data;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class DataStore {

    private static DataStore instance;

    private final ObservableList<Responsavel> responsaveis = FXCollections.observableArrayList();
    private final ObservableList<Local> locais = FXCollections.observableArrayList();
    private final ObservableList<Equipamento> equipamentos = FXCollections.observableArrayList();
    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private final ObservableList<Venda> vendas = FXCollections.observableArrayList();
    private final ObservableList<Compra> compras = FXCollections.observableArrayList();

    private int vendaSeq = 0;

    private DataStore() {
        // Sem seed: o projeto está usando Controller -> Service -> DAO -> Banco.
        // Esta classe fica apenas para telas antigas que ainda dependem dela compilar.
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public ObservableList<Responsavel> getResponsaveis() {
        return responsaveis;
    }

    public ObservableList<Local> getLocais() {
        return locais;
    }

    public ObservableList<Equipamento> getEquipamentos() {
        return equipamentos;
    }

    public ObservableList<Cliente> getClientes() {
        return clientes;
    }

    public ObservableList<Venda> getVendas() {
        return vendas;
    }

    public ObservableList<Compra> getCompras() {
        return compras;
    }

    public String nextNumeroVenda() {
        vendaSeq++;
        return "V" + vendaSeq;
    }

    public int totalUnidadesEstoque() {
        return equipamentos.stream()
                .mapToInt(Equipamento::getQuantidadeEstoque)
                .sum();
    }

    public double valorTotalEstoque() {
        return equipamentos.stream()
                .mapToDouble(e -> e.getPreco() * e.getQuantidadeEstoque())
                .sum();
    }

    public long vendasAtivasCount() {
        return vendas.size();
    }

    public double totalVendasAtivas() {
        return vendas.stream()
                .map(Venda::getValorTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }
}
