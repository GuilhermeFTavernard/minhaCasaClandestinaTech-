package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Local;
import com.minhacasa.estoque.model.Responsavel;
import com.minhacasa.estoque.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LocalFormController {

    @FXML private Label titleLabel;
    @FXML private TextField casaField;
    @FXML private TextField compartimentoField;
    @FXML private ComboBox<Responsavel> responsavelCombo;

    private final DataStore store = DataStore.getInstance();
    private Local editando;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        responsavelCombo.setItems(store.getResponsaveis());
    }

    public void iniciar(Local existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Novo Local");
        } else {
            titleLabel.setText("Editar Local");
            casaField.setText(existente.getCasa());
            compartimentoField.setText(existente.getCompartimento());
            responsavelCombo.setValue(existente.getResponsavel());
        }
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        String casa = casaField.getText() == null ? "" : casaField.getText().trim();
        String compartimento = compartimentoField.getText() == null ? "" : compartimentoField.getText().trim();

        if (casa.isEmpty()) {
            AlertUtil.erro("Informe a casa.");
            return;
        }
        if (compartimento.isEmpty()) {
            AlertUtil.erro("Informe o compartimento.");
            return;
        }

        if (editando == null) {
            Local novo = new Local(casa, compartimento, responsavelCombo.getValue());
            store.getLocais().add(novo);
        } else {
            editando.setCasa(casa);
            editando.setCompartimento(compartimento);
            editando.setResponsavel(responsavelCombo.getValue());
        }

        if (onSaved != null) {
            onSaved.run();
        }
        AppContext.getMainController().hideModal();
    }
}
