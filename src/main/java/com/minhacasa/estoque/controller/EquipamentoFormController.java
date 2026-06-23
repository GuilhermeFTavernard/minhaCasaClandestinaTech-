package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.model.Local;
import com.minhacasa.estoque.model.Responsavel;
import com.minhacasa.estoque.util.AlertUtil;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller do modal "Novo/Editar Equipamento".
 * Este e o padrao replicado nos demais formularios de cadastro/edicao do sistema.
 */
public class EquipamentoFormController {

    @FXML private Label titleLabel;
    @FXML private TextField nomeField;
    @FXML private TextField serieField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeField;
    @FXML private ComboBox<Local> localCombo;
    @FXML private ComboBox<Responsavel> responsavelCombo;

    private final DataStore store = DataStore.getInstance();
    private Equipamento editando;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        localCombo.setItems(store.getLocais());
        responsavelCombo.setItems(store.getResponsaveis());
    }

    /**
     * @param existente equipamento a editar, ou null para cadastrar um novo
     * @param onSaved   callback executado apos salvar com sucesso (ex.: atualizar a tabela)
     */
    public void iniciar(Equipamento existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Novo Equipamento");
        } else {
            titleLabel.setText("Editar Equipamento");
            nomeField.setText(existente.getNome());
            serieField.setText(existente.getNumeroSerie());
            precoField.setText(formatarNumero(existente.getPreco()));
            quantidadeField.setText(String.valueOf(existente.getQuantidade()));
            localCombo.setValue(existente.getLocal());
            responsavelCombo.setValue(existente.getResponsavel());
        }
    }

    private String formatarNumero(double valor) {
        if (valor == Math.floor(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();
        String serie = serieField.getText() == null ? "" : serieField.getText().trim();

        if (nome.isEmpty()) {
            AlertUtil.erro("Informe o nome do equipamento.");
            return;
        }
        if (serie.isEmpty()) {
            AlertUtil.erro("Informe o numero de serie.");
            return;
        }

        double preco = FormatUtil.parseDoubleOrZero(precoField.getText());
        int quantidade = FormatUtil.parseIntOrZero(quantidadeField.getText());

        if (preco <= 0) {
            AlertUtil.erro("Informe um preco valido, maior que zero.");
            return;
        }
        if (quantidade <= 0) {
            AlertUtil.erro("Informe uma quantidade valida, maior que zero.");
            return;
        }

        if (editando == null) {
            Equipamento novo = new Equipamento(nome, serie, preco, quantidade,
                    localCombo.getValue(), responsavelCombo.getValue());
            store.getEquipamentos().add(novo);
        } else {
            editando.setNome(nome);
            editando.setNumeroSerie(serie);
            editando.setPreco(preco);
            editando.setQuantidade(quantidade);
            editando.setLocal(localCombo.getValue());
            editando.setResponsavel(responsavelCombo.getValue());
        }

        if (onSaved != null) {
            onSaved.run();
        }
        AppContext.getMainController().hideModal();
    }
}
