package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Cliente;
import com.minhacasa.estoque.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ClienteFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField enderecoField;

    private final DataStore store = DataStore.getInstance();
    private Cliente editando;
    private Runnable onSaved;

    public void iniciar(Cliente existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Novo Cliente");
        } else {
            titleLabel.setText("Editar Cliente");
            nomeField.setText(existente.getNome());
            cpfField.setText(existente.getCpf());
            enderecoField.setText(existente.getEndereco());
        }
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();
        String cpf = cpfField.getText() == null ? "" : cpfField.getText().trim();

        if (nome.isEmpty()) {
            AlertUtil.erro("Informe o nome do cliente.");
            return;
        }
        if (cpf.isEmpty()) {
            AlertUtil.erro("Informe o CPF do cliente.");
            return;
        }

        String endereco = enderecoField.getText() == null ? "" : enderecoField.getText().trim();

        if (editando == null) {
            Cliente novo = new Cliente(nome, cpf, endereco);
            store.getClientes().add(novo);
        } else {
            editando.setNome(nome);
            editando.setCpf(cpf);
            editando.setEndereco(endereco);
        }

        if (onSaved != null) {
            onSaved.run();
        }
        AppContext.getMainController().hideModal();
    }
}
