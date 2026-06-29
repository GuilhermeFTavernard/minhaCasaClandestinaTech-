package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ClienteService;
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

public class ClientesController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Cliente> tabela;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCpf;
    @FXML private TableColumn<Cliente, String> colEndereco;
    @FXML private TableColumn<Cliente, Void> colAcoes;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));

        colCpf.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getCpf())));

        colEndereco.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEndereco()));

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✎");
            private final Button delBtn = new Button("🗑");
            private final HBox box = new HBox(4, editBtn, delBtn);

            {
                editBtn.getStyleClass().add("btn-icon-edit");
                delBtn.getStyleClass().add("btn-icon-delete");
                box.setAlignment(Pos.CENTER_LEFT);

                editBtn.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    abrirModal(cliente);
                });

                delBtn.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    excluir(cliente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        carregarClientes();
    }

    private void carregarClientes() {
        List<Cliente> clientes = clienteService.listar();
        tabela.setItems(FXCollections.observableArrayList(clientes));
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = tabela.getItems() == null ? 0 : tabela.getItems().size();
        subtitleLabel.setText(total + (total == 1 ? " cliente cadastrado" : " clientes cadastrados"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Cliente existente) {
        ClienteFormController controller = AppContext.getMainController().showModal("cliente_form.fxml");

        controller.iniciar(existente, () -> {
            carregarClientes();
            AppContext.getMainController().refreshCurrentView();
        });
    }

    private void excluir(Cliente cliente) {
        if (cliente == null) {
            return;
        }

        boolean ok = AlertUtil.confirmar(
                "Excluir cliente",
                "Tem certeza que deseja excluir \"" + cliente.getNome() + "\"?"
        );

        if (ok) {
            clienteService.deletar(cliente);
            carregarClientes();
        }
    }
}
