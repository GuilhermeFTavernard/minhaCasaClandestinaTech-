package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Local;
import com.minhacasa.estoque.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class LocaisController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Local> tabela;
    @FXML private TableColumn<Local, String> colCasa;
    @FXML private TableColumn<Local, String> colCompartimento;
    @FXML private TableColumn<Local, String> colResponsavel;
    @FXML private TableColumn<Local, Void> colAcoes;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colCasa.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCasa()));
        colCompartimento.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompartimento()));
        colResponsavel.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getResponsavel() == null ? "" : c.getValue().getResponsavel().getNome()));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✎");
            private final Button delBtn = new Button("🗑");
            private final HBox box = new HBox(4, editBtn, delBtn);
            {
                editBtn.getStyleClass().add("btn-icon-edit");
                delBtn.getStyleClass().add("btn-icon-delete");
                box.setAlignment(Pos.CENTER_LEFT);
                editBtn.setOnAction(e -> abrirModal(getTableRow().getItem()));
                delBtn.setOnAction(e -> excluir(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        tabela.setItems(store.getLocais());
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = store.getLocais().size();
        subtitleLabel.setText(total + (total == 1 ? " local cadastrado" : " locais cadastrados"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Local existente) {
        LocalFormController controller = AppContext.getMainController().showModal("local_form.fxml");
        controller.iniciar(existente, () -> AppContext.getMainController().refreshCurrentView());
    }

    private void excluir(Local local) {
        if (local == null) {
            return;
        }
        boolean ok = AlertUtil.confirmar("Excluir local",
                "Tem certeza que deseja excluir \"" + local.getDescricao() + "\"?");
        if (ok) {
            store.getLocais().remove(local);
            AppContext.getMainController().refreshCurrentView();
        }
    }
}
