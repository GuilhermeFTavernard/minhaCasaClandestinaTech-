package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Responsavel;
import com.minhacasa.estoque.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ResponsavelFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField enderecoField;
    @FXML private TextField telefoneField;

    private final DataStore store = DataStore.getInstance();
    private Responsavel editando;
    private Runnable onSaved;

    public void iniciar(Responsavel existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Novo Responsável");
        } else {
            titleLabel.setText("Editar Responsável");
            nomeField.setText(existente.getNome());
            enderecoField.setText(existente.getEndereco());
            telefoneField.setText(existente.getTelefone());
        }
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();
        if (nome.isEmpty()) {
            AlertUtil.erro("Informe o nome do responsável.");
            return;
        }

        String endereco = enderecoField.getText() == null ? "" : enderecoField.getText().trim();
        String telefone = telefoneField.getText() == null ? "" : telefoneField.getText().trim();

        if (editando == null) {
            Responsavel novo = new Responsavel(nome, endereco, telefone);
            store.getResponsaveis().add(novo);
        } else {
            editando.setNome(nome);
            editando.setEndereco(endereco);
            editando.setTelefone(telefone);
        }

        if (onSaved != null) {
            onSaved.run();
        }
        AppContext.getMainController().hideModal();
    }
}
