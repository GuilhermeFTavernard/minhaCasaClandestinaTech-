package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;

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
        titleLabel.setText("Nota de Venda #" + venda.getIdVenda());
        dataLabel.setText(FormatUtil.date(venda.getDataVenda()));
        numeroLabel.setText(String.valueOf(venda.getIdVenda()));
        clienteLabel.setText(venda.getCliente() == null ? "" : venda.getCliente().getNome());
        cpfLabel.setText(venda.getCliente() == null ? "" : String.valueOf(venda.getCliente().getCpf()));

        itensBox.getChildren().clear();

        for (ItemVenda item : venda.getItensVenda()) {
            HBox row = new HBox();
            row.setAlignment(Pos.CENTER_LEFT);

            String nomeEquip = item.getEquipamento() == null ? "" : item.getEquipamento().getNome();
            Label nome = new Label(nomeEquip + "  ×  " + item.getQuantidade());
            nome.getStyleClass().add("receipt-value");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label subtotal = new Label(moeda(item.calcularSubtotal()));
            subtotal.getStyleClass().add("receipt-value");

            row.getChildren().addAll(nome, spacer, subtotal);
            itensBox.getChildren().add(row);
        }

        totalLabel.setText(moeda(venda.getValorTotal()));
        statusLabel.setText("Status: REGISTRADA");
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

    private String moeda(BigDecimal valor) {
        return FormatUtil.currency(valor == null ? 0.0 : valor.doubleValue());
    }
}
