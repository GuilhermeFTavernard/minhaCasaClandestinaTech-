package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Cliente;
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

public class ClientesController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Cliente> tabela;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpf;
    @FXML private TableColumn<Cliente, String> colEndereco;
    @FXML private TableColumn<Cliente, Void> colAcoes;

    private final DataStore store = DataStore.getInstance();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colCpf.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCpf()));
        colEndereco.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEndereco()));

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

        tabela.setItems(store.getClientes());
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = store.getClientes().size();
        subtitleLabel.setText(total + (total == 1 ? " cliente cadastrado" : " clientes cadastrados"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Cliente existente) {
        ClienteFormController controller = AppContext.getMainController().showModal("cliente_form.fxml");
        controller.iniciar(existente, () -> AppContext.getMainController().refreshCurrentView());
    }

    private void excluir(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        boolean usado = store.getVendas().stream().anyMatch(v -> v.getCliente() == cliente);
        if (usado) {
            AlertUtil.erro("Este cliente possui vendas registradas e não pode ser excluído.");
            return;
        }
        boolean ok = AlertUtil.confirmar("Excluir cliente",
                "Tem certeza que deseja excluir \"" + cliente.getNome() + "\"?");
        if (ok) {
            store.getClientes().remove(cliente);
            AppContext.getMainController().refreshCurrentView();
        }
    }
}
