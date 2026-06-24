package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.VendaService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.util.List;

public class VendasController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Venda> tabela;
    @FXML private TableColumn<Venda, String> colNumero;
    @FXML private TableColumn<Venda, String> colData;
    @FXML private TableColumn<Venda, String> colCliente;
    @FXML private TableColumn<Venda, String> colItens;
    @FXML private TableColumn<Venda, String> colTotal;
    @FXML private TableColumn<Venda, String> colStatus;
    @FXML private TableColumn<Venda, Void> colAcoes;

    private final VendaService vendaService = new VendaService();
    private final EquipamentoService equipamentoService = new EquipamentoService();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNumero.setCellValueFactory(c ->
                new SimpleStringProperty("#" + c.getValue().getIdVenda()));

        colData.setCellValueFactory(c ->
                new SimpleStringProperty(FormatUtil.date(c.getValue().getDataVenda())));

        colCliente.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getCliente() == null ? "" : c.getValue().getCliente().getNome()
                ));

        colItens.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getItensVenda().size())));

        colTotal.setCellValueFactory(c -> {
            BigDecimal total = c.getValue().getValorTotal();
            return new SimpleStringProperty(FormatUtil.currency(total == null ? 0.0 : total.doubleValue()));
        });

        // Sua entidade Venda atual não possui StatusVenda.
        colStatus.setCellValueFactory(c -> new SimpleStringProperty("REGISTRADA"));

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(status);
                    badge.getStyleClass().add("badge-ativa");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button verBtn = new Button("📄");
            private final Button editBtn = new Button("✎");
            private final Button cancelarBtn = new Button("✕");
            private final HBox box = new HBox(4);

            {
                verBtn.getStyleClass().add("btn-icon-view");
                editBtn.getStyleClass().add("btn-icon-edit");
                cancelarBtn.getStyleClass().add("btn-icon-cancel");
                box.setAlignment(Pos.CENTER_LEFT);

                verBtn.setOnAction(e -> {
                    Venda venda = getTableView().getItems().get(getIndex());
                    abrirNota(venda);
                });

                editBtn.setOnAction(e -> {
                    Venda venda = getTableView().getItems().get(getIndex());
                    abrirModal(venda);
                });

                cancelarBtn.setOnAction(e -> {
                    Venda venda = getTableView().getItems().get(getIndex());
                    cancelar(venda);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                box.getChildren().setAll(verBtn, editBtn, cancelarBtn);
                setGraphic(box);
            }
        });

        carregarVendas();
    }

    private void carregarVendas() {
        List<Venda> vendas = vendaService.listar();
        tabela.setItems(FXCollections.observableArrayList(vendas));
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = tabela.getItems() == null ? 0 : tabela.getItems().size();
        subtitleLabel.setText(total + (total == 1 ? " venda registrada" : " vendas registradas"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Venda existente) {
        VendaFormController controller = AppContext.getMainController().showModal("venda_form.fxml");

        controller.iniciar(existente, () -> {
            carregarVendas();
            AppContext.getMainController().refreshCurrentView();
        });
    }

    private void abrirNota(Venda venda) {
        if (venda == null) {
            return;
        }

        NotaVendaController controller = AppContext.getMainController().showModal("nota_venda.fxml");
        controller.iniciar(venda);
    }

    private void cancelar(Venda venda) {
        if (venda == null) {
            return;
        }

        boolean ok = AlertUtil.confirmar(
                "Cancelar venda",
                "Tem certeza que deseja cancelar a venda #" + venda.getIdVenda() + "? Os itens voltarão para o estoque."
        );

        if (ok) {
            for (ItemVenda item : venda.getItensVenda()) {
                Equipamento equipamento = item.getEquipamento();

                if (equipamento != null) {
                    equipamento.setQuantidadeEstoque(
                            equipamento.getQuantidadeEstoque() + item.getQuantidade()
                    );
                    equipamentoService.alterar(equipamento);
                }
            }

            vendaService.deletar(venda);
            carregarVendas();
        }
    }
}
