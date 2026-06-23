package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.model.ItemVenda;
import com.minhacasa.estoque.model.StatusVenda;
import com.minhacasa.estoque.model.Venda;
import com.minhacasa.estoque.util.AlertUtil;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

/** Listagem de vendas: numero, data, cliente, itens, total, status e acoes condicionais. */
public class VendasController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Venda> tabela;
    @FXML private TableColumn<Venda, String> colNumero;
    @FXML private TableColumn<Venda, String> colData;
    @FXML private TableColumn<Venda, String> colCliente;
    @FXML private TableColumn<Venda, String> colItens;
    @FXML private TableColumn<Venda, String> colTotal;
    @FXML private TableColumn<Venda, StatusVenda> colStatus;
    @FXML private TableColumn<Venda, Void> colAcoes;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNumero.setCellValueFactory(c -> new SimpleStringProperty("#" + c.getValue().getNumero()));
        colData.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.date(c.getValue().getData())));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getCliente() == null ? "" : c.getValue().getCliente().getNome()));
        colItens.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQuantidadeItens())));
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().getTotal())));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getStatus()));

        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(StatusVenda status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(status.getLabel());
                    badge.getStyleClass().add(status == StatusVenda.ATIVA ? "badge-ativa" : "badge-cancelada");
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
                verBtn.setOnAction(e -> abrirNota(getTableRow().getItem()));
                editBtn.setOnAction(e -> abrirModal(getTableRow().getItem()));
                cancelarBtn.setOnAction(e -> cancelar(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }
                Venda venda = getTableRow().getItem();
                box.getChildren().setAll(verBtn);
                if (venda.getStatus() == StatusVenda.ATIVA) {
                    box.getChildren().addAll(editBtn, cancelarBtn);
                }
                setGraphic(box);
            }
        });

        tabela.setItems(store.getVendas());
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        long ativas = store.vendasAtivasCount();
        subtitleLabel.setText(ativas + (ativas == 1 ? " venda ativa" : " vendas ativas"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Venda existente) {
        VendaFormController controller = AppContext.getMainController().showModal("venda_form.fxml");
        controller.iniciar(existente, () -> AppContext.getMainController().refreshCurrentView());
    }

    private void abrirNota(Venda venda) {
        if (venda == null) {
            return;
        }
        NotaVendaController controller = AppContext.getMainController().showModal("nota_venda.fxml");
        controller.iniciar(venda);
    }

    private void cancelar(Venda venda) {
        if (venda == null || venda.getStatus() != StatusVenda.ATIVA) {
            return;
        }
        boolean ok = AlertUtil.confirmar("Cancelar venda",
                "Tem certeza que deseja cancelar a venda #" + venda.getNumero() + "? "
                        + "Os itens voltarão para o estoque.");
        if (ok) {
            // devolve os itens ao estoque
            for (ItemVenda item : venda.getItens()) {
                Equipamento eq = item.getEquipamento();
                if (eq != null) {
                    eq.setQuantidade(eq.getQuantidade() + item.getQuantidade());
                }
            }
            venda.setStatus(StatusVenda.CANCELADA);
            AppContext.getMainController().refreshCurrentView();
        }
    }
}
