package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ResponsavelService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ResponsavelFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField enderecoField;
    @FXML private TextField telefoneField;

    private final ResponsavelService responsavelService = new ResponsavelService();
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
        try {
            String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();
            String endereco = enderecoField.getText() == null ? "" : enderecoField.getText().trim();
            String telefone = telefoneField.getText() == null ? "" : telefoneField.getText().trim();

            if (nome.isEmpty()) {
                AlertUtil.erro("Informe o nome do responsável.");
                return;
            }

            if (endereco.isEmpty()) {
                AlertUtil.erro("Informe o endereço do responsável.");
                return;
            }

            if (telefone.isEmpty()) {
                AlertUtil.erro("Informe o telefone do responsável.");
                return;
            }

            if (editando == null) {
                Responsavel novo = new Responsavel(nome, endereco, telefone);
                responsavelService.cadastrar(novo);
            } else {
                editando.setNome(nome);
                editando.setEndereco(endereco);
                editando.setTelefone(telefone);
                responsavelService.alterar(editando);
            }

            if (onSaved != null) {
                onSaved.run();
            }

            AppContext.getMainController().hideModal();

        } catch (RuntimeException e) {
            AlertUtil.erro(e.getMessage());
        }
    }
}
