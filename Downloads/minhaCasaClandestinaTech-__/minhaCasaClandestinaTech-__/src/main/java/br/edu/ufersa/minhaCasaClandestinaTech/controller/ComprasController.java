package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Compra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemCompra;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.CompraService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Comparator;
import java.util.List;

public class ComprasController {

    @FXML private Label subtitleLabel;
    @FXML private TableView<Compra> tabela;
    @FXML private TableColumn<Compra, String> colData;
    @FXML private TableColumn<Compra, String> colEquipamento;
    @FXML private TableColumn<Compra, String> colFornecedor;
    @FXML private TableColumn<Compra, String> colQtd;
    @FXML private TableColumn<Compra, String> colCusto;
    @FXML private TableColumn<Compra, String> colTotal;
    @FXML private TableColumn<Compra, String> colTipo;

    private final CompraService compraService = new CompraService();

    @FXML
    public void initialize() {
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colData.setCellValueFactory(c ->
                new SimpleStringProperty(FormatUtil.date(c.getValue().getDataCompra())));

        colEquipamento.setCellValueFactory(c ->
                new SimpleStringProperty(descreverEquipamento(c.getValue())));

        colFornecedor.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getFornecedor() == null || c.getValue().getFornecedor().isBlank()
                                ? "-"
                                : c.getValue().getFornecedor()
                ));

        colQtd.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(totalQuantidade(c.getValue()))));

        colCusto.setCellValueFactory(c ->
                new SimpleStringProperty(FormatUtil.currency(custoMedio(c.getValue()))));

        colTotal.setCellValueFactory(c ->
                new SimpleStringProperty(FormatUtil.currency(c.getValue().calcularTotal())));

        colTipo.setCellValueFactory(c -> new SimpleStringProperty("entrada"));

        colTipo.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label("↘ " + item);
                    badge.getStyleClass().add("badge-entrada");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        carregarCompras();
    }

    private void carregarCompras() {
        List<Compra> compras = compraService.listar();
        compras.sort(Comparator.comparing(Compra::getDataCompra).reversed());

        tabela.setItems(FXCollections.observableArrayList(compras));
        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = tabela.getItems() == null ? 0 : tabela.getItems().size();
        subtitleLabel.setText(total + (total == 1 ? " entrada registrada" : " entradas registradas"));
    }

    @FXML
    private void onNovo() {
        CompraFormController controller = AppContext.getMainController().showModal("compra_form.fxml");

        controller.iniciar(() -> {
            carregarCompras();
            AppContext.getMainController().refreshCurrentView();
        });
    }

    private String descreverEquipamento(Compra compra) {
        if (compra.getListaDeItens() == null || compra.getListaDeItens().isEmpty()) {
            return "-";
        }

        if (compra.getListaDeItens().size() == 1) {
            ItemCompra item = compra.getListaDeItens().get(0);

            if (item.getEquipamento() != null) {
                return item.getEquipamento().getNome();
            }

            return item.getCategoria() == null ? "-" : item.getCategoria();
        }

        return compra.getListaDeItens().size() + " itens";
    }

    private int totalQuantidade(Compra compra) {
        if (compra.getListaDeItens() == null) {
            return 0;
        }

        return compra.getListaDeItens()
                .stream()
                .mapToInt(ItemCompra::getQuantidade)
                .sum();
    }

    private double custoMedio(Compra compra) {
        int quantidadeTotal = totalQuantidade(compra);

        if (quantidadeTotal <= 0) {
            return 0.0;
        }

        return compra.calcularTotal() / quantidadeTotal;
    }
}
