package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ClienteService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ClienteFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField enderecoField;

    private final ClienteService clienteService = new ClienteService(); // ← usa o service correto
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
        String nome     = nomeField.getText()     == null ? "" : nomeField.getText().trim();
        String cpf      = cpfField.getText()      == null ? "" : cpfField.getText().trim();
        String endereco = enderecoField.getText()  == null ? "" : enderecoField.getText().trim();

        if (nome.isEmpty()) {
            AlertUtil.erro("Informe o nome do cliente.");
            return;
        }
        if (cpf.isEmpty()) {
            AlertUtil.erro("Informe o CPF do cliente.");
            return;
        }
        if (endereco.isEmpty()) {
            AlertUtil.erro("Informe o endereço do cliente.");
            return;
        }

        try {
            if (editando == null) {
                // ← ordem correta: nome, endereco, cpf
                Cliente novo = new Cliente(nome, endereco, cpf);
                clienteService.cadastrar(novo);
            } else {
                editando.setNome(nome);
                editando.setEndereco(endereco);
                editando.setCpf(cpf);
                clienteService.alterar(editando);
            }

            if (onSaved != null) onSaved.run();
            AppContext.getMainController().hideModal();

        } catch (Exception e) {
            AlertUtil.erro("Erro ao salvar cliente: " + e.getMessage());
        }
    }
}