package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.facade.CompraFacade;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.CompraService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;

import java.time.LocalDate;

public class CompraFormController {

    @FXML private ComboBox<Equipamento> equipamentoCombo;
    @FXML private TextField fornecedorField;
    @FXML private TextField quantidadeField;
    @FXML private TextField custoField;
    @FXML private DatePicker dataPicker;

    private final CompraService compraService = new CompraService();
    private final CompraFacade compraFacade = new CompraFacade();

    private Runnable onSaved;

    @FXML
    public void initialize() {
        equipamentoCombo.setItems(
                FXCollections.observableArrayList(compraService.listarEquipamentos())
        );

        equipamentoCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Equipamento e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? null : e.getNome());
            }
        });
        equipamentoCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Equipamento e, boolean empty) {
                super.updateItem(e, empty);
                setText(empty || e == null ? null : e.getNome());
            }
        });

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
        try {
            Equipamento equipamento = equipamentoCombo.getValue();

            String fornecedor = fornecedorField.getText() == null
                    ? ""
                    : fornecedorField.getText().trim();

            int quantidade = FormatUtil.parseIntOrZero(quantidadeField.getText());
            float custoUnitario = (float) FormatUtil.parseDoubleOrZero(custoField.getText());
            LocalDate dataCompra = dataPicker.getValue();

            if (equipamento == null) {
                AlertUtil.erro("Selecione o equipamento.");
                return;
            }

            if (fornecedor.isBlank()) {
                AlertUtil.erro("Informe o fornecedor.");
                return;
            }

            if (dataCompra == null) {
                AlertUtil.erro("Selecione a data da compra.");
                return;
            }

            if (quantidade <= 0) {
                AlertUtil.erro("Informe uma quantidade válida.");
                return;
            }

            if (custoUnitario < 0) {
                AlertUtil.erro("Informe um custo válido.");
                return;
            }

            Compra compra = new Compra(dataCompra, fornecedor);

            ItemCompra item = new ItemCompra(
                    quantidade,
                    custoUnitario,
                    equipamento.getNome(),
                    equipamento
            );

            compra.adicionarItemDeCompra(item);

            compraFacade.realizarCompra(compra);

            if (onSaved != null) {
                onSaved.run();
            }

            AppContext.getMainController().hideModal();

        } catch (RuntimeException e) {
            AlertUtil.erro(e.getMessage());
        }
    }
}
