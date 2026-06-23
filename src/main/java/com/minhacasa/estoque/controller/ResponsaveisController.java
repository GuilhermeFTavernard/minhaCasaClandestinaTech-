package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Responsavel;
import com.minhacasa.estoque.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class ResponsaveisController {

    @FXML private TableView<Responsavel> tabela;
    @FXML private TableColumn<Responsavel, String> colNome;
    @FXML private TableColumn<Responsavel, String> colEndereco;
    @FXML private TableColumn<Responsavel, String> colTelefone;
    @FXML private TableColumn<Responsavel, Void> colAcoes;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colEndereco.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEndereco()));
        colTelefone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));

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

        tabela.setItems(store.getResponsaveis());
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Responsavel existente) {
        ResponsavelFormController controller = AppContext.getMainController().showModal("responsavel_form.fxml");
        controller.iniciar(existente, () -> AppContext.getMainController().refreshCurrentView());
    }

    private void excluir(Responsavel responsavel) {
        if (responsavel == null) {
            return;
        }
        boolean usado = store.getEquipamentos().stream().anyMatch(e -> e.getResponsavel() == responsavel)
                || store.getLocais().stream().anyMatch(l -> l.getResponsavel() == responsavel);
        if (usado) {
            AlertUtil.erro("Este responsável está vinculado a equipamentos ou locais e não pode ser excluído.");
            return;
        }
        boolean ok = AlertUtil.confirmar("Excluir responsável",
                "Tem certeza que deseja excluir \"" + responsavel.getNome() + "\"?");
        if (ok) {
            store.getResponsaveis().remove(responsavel);
            AppContext.getMainController().refreshCurrentView();
        }
    }
}
