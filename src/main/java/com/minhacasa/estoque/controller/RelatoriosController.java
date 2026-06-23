package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.model.ItemVenda;
import com.minhacasa.estoque.model.StatusVenda;
import com.minhacasa.estoque.model.Venda;
import com.minhacasa.estoque.util.FormatUtil;
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

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller da tela de Relatorios, com duas abas simuladas por ToggleButtons:
 * "Vendas Por Periodo" (filtro de datas, indicadores, equipamentos mais
 * vendidos e detalhamento das vendas) e "Relatorio De Estoque" (posicao
 * atual do estoque).
 */
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

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        configurarAbas();
        configurarTabelaMaisVendidos();
        configurarTabelaEstoque();

        dataDePicker.setValue(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        dataAtePicker.setValue(LocalDate.now());
        dataDePicker.valueProperty().addListener((obs, oldV, newV) -> recalcularVendas());
        dataAtePicker.valueProperty().addListener((obs, oldV, newV) -> recalcularVendas());

        totalItensLabel.setText(String.valueOf(store.getEquipamentos().size()));
        totalUnidadesLabel.setText(String.valueOf(store.totalUnidadesEstoque()));
        valorEstoqueLabel.setText(FormatUtil.currency(store.valorTotalEstoque()));

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
        colReceitaMV.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().receita)));

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
        colSerie.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNumeroSerie()));
        colQtd.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQuantidade())));
        colPrecoUnit.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().getPreco())));
        colValorTotal.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().getValorTotal())));
        colLocal.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getLocal() == null ? "" : c.getValue().getLocal().getDescricao()));
        colResponsavel.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getResponsavel() == null ? "" : c.getValue().getResponsavel().getNome()));

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

        tabelaEstoque.setItems(store.getEquipamentos());
    }

    private void recalcularVendas() {
        LocalDate de = dataDePicker.getValue();
        LocalDate ate = dataAtePicker.getValue();

        List<Venda> vendasFiltradas = store.getVendas().stream()
                .filter(v -> v.getStatus() == StatusVenda.ATIVA)
                .filter(v -> de == null || !v.getData().isBefore(de))
                .filter(v -> ate == null || !v.getData().isAfter(ate))
                .sorted(Comparator.comparing(Venda::getData))
                .collect(Collectors.toList());

        int qtdVendas = vendasFiltradas.size();
        int itensVendidos = vendasFiltradas.stream().mapToInt(Venda::getQuantidadeItens).sum();
        double receitaTotal = vendasFiltradas.stream().mapToDouble(Venda::getTotal).sum();

        vendasPeriodoLabel.setText(String.valueOf(qtdVendas));
        itensVendidosLabel.setText(String.valueOf(itensVendidos));
        receitaTotalLabel.setText(FormatUtil.currency(receitaTotal));

        Map<Equipamento, Integer> qtdPorEquip = new LinkedHashMap<>();
        Map<Equipamento, Double> receitaPorEquip = new LinkedHashMap<>();
        for (Venda venda : vendasFiltradas) {
            for (ItemVenda item : venda.getItens()) {
                Equipamento eq = item.getEquipamento();
                if (eq == null) {
                    continue;
                }
                qtdPorEquip.merge(eq, item.getQuantidade(), Integer::sum);
                receitaPorEquip.merge(eq, item.getSubtotal(), Double::sum);
            }
        }

        List<MaisVendidoRow> linhas = qtdPorEquip.entrySet().stream()
                .map(e -> new MaisVendidoRow(e.getKey().getNome(), e.getValue(),
                        receitaPorEquip.getOrDefault(e.getKey(), 0.0)))
                .sorted((a, b) -> Integer.compare(b.quantidade, a.quantidade))
                .collect(Collectors.toList());

        tabelaMaisVendidos.setItems(FXCollections.observableArrayList(linhas));

        montarDetalhamento(vendasFiltradas);
    }

    private void montarDetalhamento(List<Venda> vendas) {
        detalhamentoBox.getChildren().clear();

        if (vendas.isEmpty()) {
            Label vazio = new Label("Nenhuma venda ativa no período selecionado.");
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

            Label numero = new Label("#" + venda.getNumero());
            numero.getStyleClass().add("section-subtitle");

            Label cliente = new Label(venda.getCliente() == null ? "" : venda.getCliente().getNome());
            cliente.getStyleClass().add("list-row-title");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label data = new Label(FormatUtil.date(venda.getData()));
            data.getStyleClass().add("list-row-subtitle");

            Label total = new Label(FormatUtil.currency(venda.getTotal()));
            total.getStyleClass().add("chip");

            header.getChildren().addAll(numero, cliente, spacer, data, total);

            VBox itensBox = new VBox(2);
            for (ItemVenda item : venda.getItens()) {
                HBox itemRow = new HBox();
                itemRow.setAlignment(Pos.CENTER_LEFT);

                String nomeEquip = item.getEquipamento() == null ? "" : item.getEquipamento().getNome();
                Label nome = new Label(nomeEquip + "  ×  " + item.getQuantidade());
                nome.getStyleClass().add("list-row-subtitle");

                Region sp = new Region();
                HBox.setHgrow(sp, Priority.ALWAYS);

                Label subtotal = new Label(FormatUtil.currency(item.getSubtotal()));
                subtotal.getStyleClass().add("list-row-subtitle");

                itemRow.getChildren().addAll(nome, sp, subtotal);
                itensBox.getChildren().add(itemRow);
            }

            card.getChildren().addAll(header, itensBox);
            detalhamentoBox.getChildren().add(card);
        }
    }

    /** Linha auxiliar para a tabela "Equipamentos Mais Vendidos". */
    private static class MaisVendidoRow {
        final String nome;
        final int quantidade;
        final double receita;

        MaisVendidoRow(String nome, int quantidade, double receita) {
            this.nome = nome;
            this.quantidade = quantidade;
            this.receita = receita;
        }
    }
}
