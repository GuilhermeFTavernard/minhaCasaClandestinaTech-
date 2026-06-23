package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.StatusVenda;
import com.minhacasa.estoque.model.Venda;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/** Controller da tela inicial (Dashboard): indicadores gerais e ultimas vendas ativas. */
public class DashboardController {

    @FXML private Label welcomeTitleLabel;

    @FXML private Label equipCountLabel;
    @FXML private Label equipSubLabel;

    @FXML private Label vendasCountLabel;
    @FXML private Label vendasSubLabel;

    @FXML private Label clientesCountLabel;
    @FXML private Label clientesSubLabel;

    @FXML private Label locaisCountLabel;
    @FXML private Label locaisSubLabel;

    @FXML private VBox ultimasVendasBox;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        welcomeTitleLabel.setText("minhaCasaClandestinaTech");

        equipCountLabel.setText(String.valueOf(store.getEquipamentos().size()));
        equipSubLabel.setText(store.totalUnidadesEstoque() + " unidades em estoque");

        vendasCountLabel.setText(String.valueOf(store.vendasAtivasCount()));
        vendasSubLabel.setText("Total: " + FormatUtil.currency(store.totalVendasAtivas()));

        clientesCountLabel.setText(String.valueOf(store.getClientes().size()));
        clientesSubLabel.setText("cadastrados");

        locaisCountLabel.setText(String.valueOf(store.getLocais().size()));
        locaisSubLabel.setText("de armazenagem");

        carregarUltimasVendas();
    }

    private void carregarUltimasVendas() {
        ultimasVendasBox.getChildren().clear();

        List<Venda> recentes = store.getVendas().stream()
                .filter(v -> v.getStatus() == StatusVenda.ATIVA)
                .sorted(Comparator.comparing(Venda::getData).reversed())
                .limit(5)
                .collect(Collectors.toList());

        if (recentes.isEmpty()) {
            Label vazio = new Label("Nenhuma venda registrada ainda.");
            vazio.getStyleClass().add("stat-sublabel");
            ultimasVendasBox.getChildren().add(vazio);
            return;
        }

        for (Venda venda : recentes) {
            HBox row = new HBox();
            row.getStyleClass().add("list-row");
            row.setAlignment(Pos.CENTER_LEFT);
            row.setSpacing(12);

            VBox info = new VBox(2);
            Label nome = new Label(venda.getCliente() == null ? "" : venda.getCliente().getNome());
            nome.getStyleClass().add("list-row-title");
            Label sub = new Label(FormatUtil.date(venda.getData()) + "  ·  " + venda.getQuantidadeItens() + " item(s)");
            sub.getStyleClass().add("list-row-subtitle");
            info.getChildren().addAll(nome, sub);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label valor = new Label(FormatUtil.currency(venda.getTotal()));
            valor.getStyleClass().add("chip");

            row.getChildren().addAll(info, spacer, valor);
            ultimasVendasBox.getChildren().add(row);
        }
    }
}
