package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.model.DAO.RelatorioDeVendasDAO;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatoriosController {

    @FXML private ToggleButton tabVendas;
    @FXML private ToggleButton tabEstoque;
    @FXML private VBox painelVendas;
    @FXML private VBox painelEstoque;

    @FXML private DatePicker dataDePicker;
    @FXML private DatePicker dataAtePicker;

    @FXML private Label vendasPeriodoLabel;
    @FXML private Label itensVendidosLabel;
    @FXML private Label receitaTotalLabel;

    @FXML private TableView<MaisVendidoRow> tabelaMaisVendidos;
    @FXML private TableColumn<MaisVendidoRow, String> colEquipMV;
    @FXML private TableColumn<MaisVendidoRow, String> colQtdMV;
    @FXML private TableColumn<MaisVendidoRow, String> colReceitaMV;

    @FXML private VBox detalhamentoBox;

    @FXML private Label totalItensLabel;
    @FXML private Label totalUnidadesLabel;
    @FXML private Label valorEstoqueLabel;

    @FXML private TableView<Equipamento> tabelaEstoque;
    @FXML private TableColumn<Equipamento, String> colEquip;
    @FXML private TableColumn<Equipamento, String> colSerie;
    @FXML private TableColumn<Equipamento, String> colQtd;
    @FXML private TableColumn<Equipamento, String> colPrecoUnit;
    @FXML private TableColumn<Equipamento, String> colValorTotal;
    @FXML private TableColumn<Equipamento, String> colLocal;
    @FXML private TableColumn<Equipamento, String> colResponsavel;

    private final RelatorioDeVendasDAO relatorioDAO = new RelatorioDeVendasDAO();
    private final EquipamentoService equipamentoService = new EquipamentoService();

    @FXML
    public void initialize() {
        configurarAbas();
        configurarTabelaMaisVendidos();
        configurarTabelaEstoque();

        dataDePicker.setValue(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        dataAtePicker.setValue(LocalDate.now());

        dataDePicker.valueProperty().addListener((obs, oldV, newV) -> recalcularVendas());
        dataAtePicker.valueProperty().addListener((obs, oldV, newV) -> recalcularVendas());

        carregarEstoque();
        recalcularVendas();
    }

    private void configurarAbas() {
        ToggleGroup grupo = new ToggleGroup();
        tabVendas.setToggleGroup(grupo);
        tabEstoque.setToggleGroup(grupo);
        tabVendas.setSelected(true);

        grupo.selectedToggleProperty().addListener((obs, oldV, newV) -> {
            boolean vendasAtivo = tabVendas.isSelected();
            painelVendas.setVisible(vendasAtivo);
            painelVendas.setManaged(vendasAtivo);
            painelEstoque.setVisible(!vendasAtivo);
            painelEstoque.setManaged(!vendasAtivo);
        });
    }

    private void configurarTabelaMaisVendidos() {
        tabelaMaisVendidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        colEquipMV.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nome));
        colQtdMV.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().quantidade)));
        colReceitaMV.setCellValueFactory(c -> new SimpleStringProperty(moeda(c.getValue().receita)));

        colQtdMV.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("badge-qty");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
    }

    private void configurarTabelaEstoque() {
        tabelaEstoque.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colEquip.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colSerie.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getNumeroSerie())));
        colQtd.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQuantidadeEstoque())));
        colPrecoUnit.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().getPreco())));
        colValorTotal.setCellValueFactory(c -> new SimpleStringProperty(
                FormatUtil.currency(c.getValue().getPreco() * c.getValue().getQuantidadeEstoque())
        ));

        // Suas entidades atuais de Equipamento não possuem Local nem Responsável.
        colLocal.setCellValueFactory(c -> new SimpleStringProperty("-"));
        colResponsavel.setCellValueFactory(c -> new SimpleStringProperty("-"));

        colQtd.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("badge-qty");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
    }

    private void carregarEstoque() {
        List<Equipamento> equipamentos = equipamentoService.listar();

        tabelaEstoque.setItems(FXCollections.observableArrayList(equipamentos));

        int totalItens = equipamentos.size();
        int totalUnidades = equipamentos.stream().mapToInt(Equipamento::getQuantidadeEstoque).sum();
        double valorEstoque = equipamentos.stream()
                .mapToDouble(e -> e.getPreco() * e.getQuantidadeEstoque())
                .sum();

        totalItensLabel.setText(String.valueOf(totalItens));
        totalUnidadesLabel.setText(String.valueOf(totalUnidades));
        valorEstoqueLabel.setText(FormatUtil.currency(valorEstoque));
    }

    private void recalcularVendas() {
        LocalDate de = dataDePicker.getValue();
        LocalDate ate = dataAtePicker.getValue();

        if (de == null || ate == null) {
            return;
        }

        List<Venda> vendasFiltradas = relatorioDAO.buscarVendasComItens(de.toString(), ate.toString());

        int qtdVendas = vendasFiltradas.size();
        int itensVendidos = vendasFiltradas.stream()
                .flatMap(v -> v.getItensVenda().stream())
                .mapToInt(ItemVenda::getQuantidade)
                .sum();

        BigDecimal receitaTotal = vendasFiltradas.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        vendasPeriodoLabel.setText(String.valueOf(qtdVendas));
        itensVendidosLabel.setText(String.valueOf(itensVendidos));
        receitaTotalLabel.setText(moeda(receitaTotal));

        Map<Integer, String> nomesPorId = new LinkedHashMap<>();
        Map<Integer, Integer> qtdPorId = new LinkedHashMap<>();
        Map<Integer, BigDecimal> receitaPorId = new LinkedHashMap<>();

        for (Venda venda : vendasFiltradas) {
            for (ItemVenda item : venda.getItensVenda()) {
                Equipamento eq = item.getEquipamento();
                if (eq == null) continue;

                int id = eq.getIdEquipamento();
                nomesPorId.putIfAbsent(id, eq.getNome());
                qtdPorId.merge(id, item.getQuantidade(), Integer::sum);
                receitaPorId.merge(id, item.calcularSubtotal(), BigDecimal::add);
            }
        }

        List<MaisVendidoRow> linhas = qtdPorId.entrySet().stream()
                .map(e -> new MaisVendidoRow(
                        nomesPorId.get(e.getKey()),
                        e.getValue(),
                        receitaPorId.getOrDefault(e.getKey(), BigDecimal.ZERO)
                ))
                .sorted((a, b) -> Integer.compare(b.quantidade, a.quantidade))
                .collect(Collectors.toList());

        tabelaMaisVendidos.setItems(FXCollections.observableArrayList(linhas));
        montarDetalhamento(vendasFiltradas);
    }

    private void montarDetalhamento(List<Venda> vendas) {
        detalhamentoBox.getChildren().clear();

        if (vendas.isEmpty()) {
            Label vazio = new Label("Nenhuma venda no período selecionado.");
            vazio.getStyleClass().add("stat-sublabel");
            detalhamentoBox.getChildren().add(vazio);
            return;
        }

        for (Venda venda : vendas) {
            VBox card = new VBox(6);
            card.getStyleClass().add("modal-divider");
            card.setStyle("-fx-padding: 0 0 14 0;");

            HBox header = new HBox(10);
            header.setAlignment(Pos.CENTER_LEFT);

            Label numero = new Label("#" + venda.getIdVenda());
            numero.getStyleClass().add("section-subtitle");

            Label cliente = new Label(venda.getCliente() == null ? "" : venda.getCliente().getNome());
            cliente.getStyleClass().add("list-row-title");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label data = new Label(FormatUtil.date(venda.getDataVenda()));
            data.getStyleClass().add("list-row-subtitle");

            Label total = new Label(moeda(venda.getValorTotal()));
            total.getStyleClass().add("chip");

            header.getChildren().addAll(numero, cliente, spacer, data, total);

            VBox itensBox = new VBox(2);
            for (ItemVenda item : venda.getItensVenda()) {
                HBox itemRow = new HBox();
                itemRow.setAlignment(Pos.CENTER_LEFT);

                String nomeEquip = item.getEquipamento() == null ? "" : item.getEquipamento().getNome();
                Label nome = new Label(nomeEquip + "  ×  " + item.getQuantidade());
                nome.getStyleClass().add("list-row-subtitle");

                Region sp = new Region();
                HBox.setHgrow(sp, Priority.ALWAYS);

                Label subtotal = new Label(moeda(item.calcularSubtotal()));
                subtotal.getStyleClass().add("list-row-subtitle");

                itemRow.getChildren().addAll(nome, sp, subtotal);
                itensBox.getChildren().add(itemRow);
            }

            card.getChildren().addAll(header, itensBox);
            detalhamentoBox.getChildren().add(card);
        }
    }

    private String moeda(BigDecimal valor) {
        return FormatUtil.currency(valor == null ? 0.0 : valor.doubleValue());
    }

    private static class MaisVendidoRow {
        final String nome;
        final int quantidade;
        final BigDecimal receita;

        MaisVendidoRow(String nome, int quantidade, BigDecimal receita) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.receita = receita;
        }
    }
}
