package com.minhacasa.estoque.data;

import com.minhacasa.estoque.model.Cliente;
import com.minhacasa.estoque.model.Compra;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.model.ItemVenda;
import com.minhacasa.estoque.model.Local;
import com.minhacasa.estoque.model.Responsavel;
import com.minhacasa.estoque.model.StatusVenda;
import com.minhacasa.estoque.model.Venda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Base de dados em memoria (singleton). Em uma aplicacao real isso seria
 * substituido por acesso a banco de dados (JDBC/JPA), mas para fins do
 * prototipo mantemos tudo em listas observaveis compartilhadas entre as telas.
 */
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
        seed();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seed() {
        Responsavel toinho = new Responsavel("Toinho", "Rua das Acacias, 14 - Centro", "(85) 99812-3456");
        Responsavel kanalense = new Responsavel("Kanalense", "Av. Bezerra de Menezes, 302", "(85) 99734-8900");
        responsaveis.addAll(toinho, kanalense);

        Local quartoMae = new Local("Casa do Toinho", "Quarto da mae", toinho);
        Local salaEstar = new Local("Casa do Toinho", "Sala de estar", toinho);
        Local garagem = new Local("Casa do Kanalense", "Garagem", kanalense);
        locais.addAll(quartoMae, salaEstar, garagem);

        Equipamento notebook = new Equipamento("Notebook Dell Inspiron 15", "DL-2024-0011", 2850, 3, quartoMae, toinho);
        Equipamento monitor = new Equipamento("Monitor LG 24\" Full HD", "LG-FHD-2244", 890, 5, salaEstar, toinho);
        Equipamento teclado = new Equipamento("Teclado Mecanico Redragon", "RD-K552-BR", 320, 8, garagem, kanalense);
        Equipamento mouse = new Equipamento("Mouse Logitech MX Master 3", "LG-MX3-0099", 480, 4, garagem, kanalense);
        Equipamento ssd = new Equipamento("SSD Kingston 480GB", "KS-SSD-480-07", 260, 10, quartoMae, toinho);
        equipamentos.addAll(notebook, monitor, teclado, mouse, ssd);

        Cliente marcos = new Cliente("Marcos Aurelio Silva", "012.345.678-90", "Rua Floriano Peixoto, 88");
        Cliente fernanda = new Cliente("Fernanda Rocha", "987.654.321-00", "Beco da Liberdade, 4");
        clientes.addAll(marcos, fernanda);

        Venda v1 = new Venda("V1", LocalDate.of(2026, 6, 10), marcos, StatusVenda.ATIVA);
        v1.getItens().add(new ItemVenda(teclado, 1, 320));
        v1.getItens().add(new ItemVenda(ssd, 2, 260));

        Venda v2 = new Venda("V2", LocalDate.of(2026, 6, 14), fernanda, StatusVenda.ATIVA);
        v2.getItens().add(new ItemVenda(monitor, 1, 890));

        Venda v3 = new Venda("V3", LocalDate.of(2026, 5, 28), marcos, StatusVenda.CANCELADA);
        v3.getItens().add(new ItemVenda(notebook, 1, 2850));

        vendas.addAll(v3, v2, v1);
        vendaSeq = 3;

        compras.add(new Compra(LocalDate.of(2026, 5, 15), notebook, "Distribuidora NordTech", 3, 2400));
        compras.add(new Compra(LocalDate.of(2026, 6, 1), ssd, "Kingston Distribuidora", 10, 200));
    }

    public ObservableList<Responsavel> getResponsaveis() { return responsaveis; }
    public ObservableList<Local> getLocais() { return locais; }
    public ObservableList<Equipamento> getEquipamentos() { return equipamentos; }
    public ObservableList<Cliente> getClientes() { return clientes; }
    public ObservableList<Venda> getVendas() { return vendas; }
    public ObservableList<Compra> getCompras() { return compras; }

    public String nextNumeroVenda() {
        vendaSeq++;
        return "V" + vendaSeq;
    }

    public int totalUnidadesEstoque() {
        return equipamentos.stream().mapToInt(Equipamento::getQuantidade).sum();
    }

    public double valorTotalEstoque() {
        return equipamentos.stream().mapToDouble(Equipamento::getValorTotal).sum();
    }

    public long vendasAtivasCount() {
        return vendas.stream().filter(v -> v.getStatus() == StatusVenda.ATIVA).count();
    }

    public double totalVendasAtivas() {
        return vendas.stream().filter(v -> v.getStatus() == StatusVenda.ATIVA)
                .mapToDouble(Venda::getTotal).sum();
    }
}
