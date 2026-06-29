package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ClienteService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.LocalService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.VendaService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    private final EquipamentoService equipamentoService = new EquipamentoService();
    private final VendaService vendaService = new VendaService();
    private final ClienteService clienteService = new ClienteService();
    private final LocalService localService = new LocalService();

    @FXML
    public void initialize() {
        welcomeTitleLabel.setText("minhaCasaClandestinaTech");

        List<Equipamento> equipamentos = equipamentoService.listar();
        List<Venda> vendas = vendaService.listar();

        int totalUnidadesEstoque = equipamentos.stream()
                .mapToInt(Equipamento::getQuantidadeEstoque)
                .sum();

        BigDecimal totalVendas = vendas.stream()
                .map(Venda::getValorTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        equipCountLabel.setText(String.valueOf(equipamentos.size()));
        equipSubLabel.setText(totalUnidadesEstoque + " unidades em estoque");

        vendasCountLabel.setText(String.valueOf(vendas.size()));
        vendasSubLabel.setText("Total: " + FormatUtil.currency(totalVendas.doubleValue()));

        clientesCountLabel.setText(String.valueOf(clienteService.listar().size()));
        clientesSubLabel.setText("cadastrados");

        locaisCountLabel.setText(String.valueOf(localService.listar().size()));
        locaisSubLabel.setText("de armazenagem");

        carregarUltimasVendas(vendas);
    }

    private void carregarUltimasVendas(List<Venda> vendas) {
        ultimasVendasBox.getChildren().clear();

        List<Venda> recentes = vendas.stream()
                .sorted(Comparator.comparing(Venda::getDataVenda).reversed())
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

            Label sub = new Label(
                    FormatUtil.date(venda.getDataVenda()) +
                            "  ·  " +
                            venda.getItensVenda().size() +
                            " item(s)"
            );
            sub.getStyleClass().add("list-row-subtitle");

            info.getChildren().addAll(nome, sub);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            double valorVenda = venda.getValorTotal() == null ? 0.0 : venda.getValorTotal().doubleValue();
            Label valor = new Label(FormatUtil.currency(valorVenda));
            valor.getStyleClass().add("chip");

            row.getChildren().addAll(info, spacer, valor);
            ultimasVendasBox.getChildren().add(row);
        }
    }
}
