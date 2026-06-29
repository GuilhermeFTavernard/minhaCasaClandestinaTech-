package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Local;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Responsavel;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.LocalService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ResponsavelService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class EquipamentoFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField serieField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeField;
    @FXML private ComboBox<Local> localCombo;
    @FXML private ComboBox<Responsavel> responsavelCombo;

    private final EquipamentoService equipamentoService = new EquipamentoService();
    private final LocalService localService = new LocalService();
    private final ResponsavelService responsavelService = new ResponsavelService();
    private Equipamento editando;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        // Popula LocalCombo
        if (localCombo != null) {
            localCombo.setItems(FXCollections.observableArrayList(localService.listar()));
            localCombo.setConverter(new StringConverter<>() {
                @Override public String toString(Local l) {
                    return l == null ? "" : l.getNomeCasa() + " / " + l.getNomeCompartimento();
                }
                @Override public Local fromString(String s) { return null; }
            });
        }

        // Popula ResponsavelCombo
        if (responsavelCombo != null) {
            responsavelCombo.setItems(FXCollections.observableArrayList(responsavelService.listar()));
            responsavelCombo.setConverter(new StringConverter<>() {
                @Override public String toString(Responsavel r) {
                    return r == null ? "" : r.getNome();
                }
                @Override public Responsavel fromString(String s) { return null; }
            });
        }
    }

    public void iniciar(Equipamento existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Novo Equipamento");
        } else {
            titleLabel.setText("Editar Equipamento");
            nomeField.setText(existente.getNome());
            serieField.setText(String.valueOf(existente.getNumeroSerie()));
            precoField.setText(formatarNumero(existente.getPreco()));
            quantidadeField.setText(String.valueOf(existente.getQuantidadeEstoque()));
            if (localCombo != null) localCombo.setValue(existente.getLocal());
            if (responsavelCombo != null) responsavelCombo.setValue(existente.getResponsavel());
        }
    }

    private String formatarNumero(double valor) {
        if (valor == Math.floor(valor)) return String.valueOf((long) valor);
        return String.valueOf(valor);
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        try {
            String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();
            String serieTexto = serieField.getText() == null ? "" : serieField.getText().trim();

            if (nome.isEmpty()) { AlertUtil.erro("Informe o nome do equipamento."); return; }
            if (serieTexto.isEmpty()) { AlertUtil.erro("Informe o número de série."); return; }

            int numeroSerie = FormatUtil.parseIntOrZero(serieTexto);
            float preco = (float) FormatUtil.parseDoubleOrZero(precoField.getText());
            int quantidade = FormatUtil.parseIntOrZero(quantidadeField.getText());

            if (numeroSerie <= 0) { AlertUtil.erro("Informe um número de série válido."); return; }
            if (preco < 0)        { AlertUtil.erro("Informe um preço válido."); return; }
            if (quantidade < 0)   { AlertUtil.erro("Informe uma quantidade válida."); return; }

            if (editando == null) {
                Equipamento novo = new Equipamento();
                novo.setNome(nome);
                novo.setNumeroSerie(numeroSerie);
                novo.setPreco(preco);
                novo.setQuantidadeEstoque(quantidade);
                if (localCombo != null) novo.setLocal(localCombo.getValue());
                if (responsavelCombo != null) novo.setResponsavel(responsavelCombo.getValue());
                equipamentoService.cadastrar(novo);
            } else {
                editando.setNome(nome);
                editando.setNumeroSerie(numeroSerie);
                editando.setPreco(preco);
                editando.setQuantidadeEstoque(quantidade);
                if (localCombo != null) editando.setLocal(localCombo.getValue());
                if (responsavelCombo != null) editando.setResponsavel(responsavelCombo.getValue());
                equipamentoService.alterar(editando);
            }

            if (onSaved != null) onSaved.run();
            AppContext.getMainController().hideModal();

        } catch (RuntimeException e) {
            AlertUtil.erro(e.getMessage());
        }
    }
}