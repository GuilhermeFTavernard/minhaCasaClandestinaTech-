package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.LocalService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ResponsavelService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class LocalFormController {

    @FXML private Label titleLabel;
    @FXML private TextField casaField;
    @FXML private TextField compartimentoField;
    @FXML private ComboBox<Responsavel> comboResponsavel;

    private final LocalService localService = new LocalService();
    private final ResponsavelService responsavelService = new ResponsavelService();
    private Local editando;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        // Popula o ComboBox com os responsáveis cadastrados
        comboResponsavel.setItems(
                FXCollections.observableArrayList(responsavelService.listar())
        );

        // Define como o nome do responsável aparece no ComboBox
        comboResponsavel.setConverter(new StringConverter<>() {
            @Override
            public String toString(Responsavel r) {
                return r == null ? "" : r.getNome();
            }
            @Override
            public Responsavel fromString(String s) { return null; }
        });
    }

    public void iniciar(Local existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Novo Local");
        } else {
            titleLabel.setText("Editar Local");
            casaField.setText(existente.getNomeCasa());
            compartimentoField.setText(existente.getNomeCompartimento());
        }
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        try {
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
                Local novo = new Local(casa, compartimento);
                novo.setResponsavel(comboResponsavel.getValue()); // ← adicionar
                localService.cadastrar(novo);
            } else {
                editando.setNomeCasa(casa);
                editando.setNomeCompartimento(compartimento);
                editando.setResponsavel(comboResponsavel.getValue()); // ← adicionar
                localService.alterar(editando);
            }

            if (onSaved != null) onSaved.run();
            AppContext.getMainController().hideModal();

        } catch (RuntimeException e) {
            AlertUtil.erro(e.getMessage());
        }
    }
}