package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Compra;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/** Listagem de compras/entradas de estoque. */
public class ComprasController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Compra> tabela;
    @FXML private TableColumn<Compra, String> colData;
    @FXML private TableColumn<Compra, String> colEquipamento;
    @FXML private TableColumn<Compra, String> colFornecedor;
    @FXML private TableColumn<Compra, String> colQtd;
    @FXML private TableColumn<Compra, String> colCusto;
    @FXML private TableColumn<Compra, String> colTotal;
    @FXML private TableColumn<Compra, String> colTipo;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colData.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.date(c.getValue().getData())));
        colEquipamento.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEquipamento() == null ? "" : c.getValue().getEquipamento().getNome()));
        colFornecedor.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFornecedor() == null || c.getValue().getFornecedor().isBlank()
                        ? "-" : c.getValue().getFornecedor()));
        colQtd.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getQuantidade())));
        colCusto.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().getCustoUnitario())));
        colTotal.setCellValueFactory(c -> new SimpleStringProperty(FormatUtil.currency(c.getValue().getTotal())));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty("entrada"));

        colTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label("↘ " + item);
                    badge.getStyleClass().add("badge-entrada");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // ordena pelas mais recentes primeiro, igual ao prototipo
        tabela.setItems(store.getCompras().sorted((a, b) -> b.getData().compareTo(a.getData())));
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = store.getCompras().size();
        subtitleLabel.setText(total + (total == 1 ? " entrada registrada" : " entradas registradas"));
    }

    @FXML
    private void onNovo() {
        CompraFormController controller = AppContext.getMainController().showModal("compra_form.fxml");
        controller.iniciar(() -> AppContext.getMainController().refreshCurrentView());
    }
}
