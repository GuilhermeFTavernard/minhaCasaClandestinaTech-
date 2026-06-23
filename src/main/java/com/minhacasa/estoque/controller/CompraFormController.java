package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Compra;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.util.AlertUtil;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;

/**
 * Controller do modal "Registrar Compra". Ao salvar, alem de registrar a
 * entrada no historico de compras, a quantidade do equipamento selecionado
 * eh incrementada no estoque (comportamento de "entrada").
 */
public class CompraFormController {

    @FXML private ComboBox<Equipamento> equipamentoCombo;
    @FXML private TextField fornecedorField;
    @FXML private TextField quantidadeField;
    @FXML private TextField custoField;
    @FXML private DatePicker dataPicker;

    private final DataStore store = DataStore.getInstance();
    private Runnable onSaved;

    @FXML
    public void initialize() {
        equipamentoCombo.setItems(store.getEquipamentos());
        dataPicker.setValue(LocalDate.now());
        quantidadeField.setText("1");
    }

    public void iniciar(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        Equipamento equipamento = equipamentoCombo.getValue();
        if (equipamento == null) {
            AlertUtil.erro("Selecione o equipamento.");
            return;
        }

        int quantidade = FormatUtil.parseIntOrZero(quantidadeField.getText());
        if (quantidade <= 0) {
            AlertUtil.erro("Informe uma quantidade válida, maior que zero.");
            return;
        }

        LocalDate data = dataPicker.getValue();
        if (data == null) {
            AlertUtil.erro("Selecione a data da compra.");
            return;
        }

        double custoUnitario = FormatUtil.parseDoubleOrZero(custoField.getText());
        String fornecedor = fornecedorField.getText() == null ? "" : fornecedorField.getText().trim();

        Compra compra = new Compra(data, equipamento, fornecedor, quantidade, custoUnitario);
        store.getCompras().add(compra);

        // entrada no estoque
        equipamento.setQuantidade(equipamento.getQuantidade() + quantidade);

        if (onSaved != null) {
            onSaved.run();
        }
        AppContext.getMainController().hideModal();
    }
}
