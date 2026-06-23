package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.model.ItemVenda;
import com.minhacasa.estoque.model.Venda;
import com.minhacasa.estoque.util.AlertUtil;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Controller do modal de visualizacao (somente leitura) da nota de venda. */
public class NotaVendaController {

    @FXML private Label titleLabel;
    @FXML private VBox receiptBox;
    @FXML private Label dataLabel;
    @FXML private Label numeroLabel;
    @FXML private Label clienteLabel;
    @FXML private Label cpfLabel;
    @FXML private VBox itensBox;
    @FXML private Label totalLabel;
    @FXML private Label statusLabel;

    public void iniciar(Venda venda) {
        titleLabel.setText("Nota de Venda #" + venda.getNumero());
        dataLabel.setText(FormatUtil.date(venda.getData()));
        numeroLabel.setText(venda.getNumero());
        clienteLabel.setText(venda.getCliente() == null ? "" : venda.getCliente().getNome());
        cpfLabel.setText(venda.getCliente() == null ? "" : venda.getCliente().getCpf());

        itensBox.getChildren().clear();
        for (ItemVenda item : venda.getItens()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);

            String nomeEquip = item.getEquipamento() == null ? "" : item.getEquipamento().getNome();
            Label nome = new Label(nomeEquip + "  ×  " + item.getQuantidade());
            nome.getStyleClass().add("receipt-value");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label subtotal = new Label(FormatUtil.currency(item.getSubtotal()));
            subtotal.getStyleClass().add("receipt-value");

            row.getChildren().addAll(nome, spacer, subtotal);
            itensBox.getChildren().add(row);
        }

        totalLabel.setText(FormatUtil.currency(venda.getTotal()));
        statusLabel.setText("Status: " + venda.getStatus().getLabel().toUpperCase());
    }

    @FXML
    private void onFechar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onImprimir() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(receiptBox.getScene().getWindow())) {
            boolean ok = job.printPage(receiptBox);
            if (ok) {
                job.endJob();
            }
        } else {
            AlertUtil.erro("Nenhuma impressora disponível neste computador.");
        }
    }
}
