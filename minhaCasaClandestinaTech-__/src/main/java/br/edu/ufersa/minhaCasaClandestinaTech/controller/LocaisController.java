package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.LocalService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
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

import java.util.List;

public class LocaisController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Local> tabela;
    @FXML private TableColumn<Local, String> colCasa;
    @FXML private TableColumn<Local, String> colCompartimento;
    @FXML private TableColumn<Local, String> colResponsavel;
    @FXML private TableColumn<Local, Void> colAcoes;

    private final LocalService localService = new LocalService();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colCasa.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNomeCasa()));

        colCompartimento.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNomeCompartimento()));

        colResponsavel.setCellValueFactory(c -> {
            var r = c.getValue().getResponsavel();
            return new SimpleStringProperty(r != null ? r.getNome() : "-");
        });

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✎");
            private final Button delBtn = new Button("🗑");
            private final HBox box = new HBox(4, editBtn, delBtn);

            {
                editBtn.getStyleClass().add("btn-icon-edit");
                delBtn.getStyleClass().add("btn-icon-delete");
                box.setAlignment(Pos.CENTER_LEFT);

                editBtn.setOnAction(e -> {
                    Local local = getTableView().getItems().get(getIndex());
                    abrirModal(local);
                });

                delBtn.setOnAction(e -> {
                    Local local = getTableView().getItems().get(getIndex());
                    excluir(local);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        carregarLocais();
    }

    private void carregarLocais() {
        List<Local> locais = localService.listar();
        tabela.setItems(FXCollections.observableArrayList(locais));
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = tabela.getItems() == null ? 0 : tabela.getItems().size();
        subtitleLabel.setText(total + (total == 1 ? " local cadastrado" : " locais cadastrados"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Local existente) {
        LocalFormController controller = AppContext.getMainController().showModal("local_form.fxml");

        controller.iniciar(existente, () -> {
            carregarLocais();
            AppContext.getMainController().refreshCurrentView();
        });
    }

    private void excluir(Local local) {
        if (local == null) {
            return;
        }

        String descricao = local.getNomeCasa() + " / " + local.getNomeCompartimento();

        boolean ok = AlertUtil.confirmar(
                "Excluir local",
                "Tem certeza que deseja excluir \"" + descricao + "\"?"
        );

        if (ok) {
            localService.deletar(local);
            carregarLocais();
        }
    }
}
