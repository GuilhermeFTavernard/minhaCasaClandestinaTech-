package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ResponsavelService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.util.List;

public class ResponsaveisController {

    @FXML private TableView<Responsavel> tabela;
    @FXML private TableColumn<Responsavel, String> colNome;
    @FXML private TableColumn<Responsavel, String> colEndereco;
    @FXML private TableColumn<Responsavel, String> colTelefone;
    @FXML private TableColumn<Responsavel, Void> colAcoes;

    private final ResponsavelService responsavelService = new ResponsavelService();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));

        colEndereco.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEndereco()));

        colTelefone.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTelefone()));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✎");
            private final Button delBtn = new Button("🗑");
            private final HBox box = new HBox(4, editBtn, delBtn);

            {
                editBtn.getStyleClass().add("btn-icon-edit");
                delBtn.getStyleClass().add("btn-icon-delete");
                box.setAlignment(Pos.CENTER_LEFT);

                editBtn.setOnAction(e -> {
                    Responsavel responsavel = getTableView().getItems().get(getIndex());
                    abrirModal(responsavel);
                });

                delBtn.setOnAction(e -> {
                    Responsavel responsavel = getTableView().getItems().get(getIndex());
                    excluir(responsavel);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        carregarResponsaveis();
    }

    private void carregarResponsaveis() {
        List<Responsavel> responsaveis = responsavelService.listar();
        tabela.setItems(FXCollections.observableArrayList(responsaveis));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Responsavel existente) {
        ResponsavelFormController controller =
                AppContext.getMainController().showModal("responsavel_form.fxml");

        controller.iniciar(existente, () -> {
            carregarResponsaveis();
            AppContext.getMainController().refreshCurrentView();
        });
    }

    private void excluir(Responsavel responsavel) {
        if (responsavel == null) {
            return;
        }

        boolean ok = AlertUtil.confirmar(
                "Excluir responsável",
                "Tem certeza que deseja excluir \"" + responsavel.getNome() + "\"?"
        );

        if (ok) {
            try {
                responsavelService.deletar(responsavel);
                carregarResponsaveis();
            } catch (RuntimeException e) {
                AlertUtil.erro(e.getMessage());
            }
        }
    }
}
