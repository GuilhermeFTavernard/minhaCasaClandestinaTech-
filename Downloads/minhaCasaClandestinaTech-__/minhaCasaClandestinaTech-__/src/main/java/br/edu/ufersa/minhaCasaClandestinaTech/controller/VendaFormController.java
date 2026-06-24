package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Cliente;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.ItemVenda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Venda;
import br.edu.ufersa.minhaCasaClandestinaTech.model.facade.VendaFacade;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.ClienteService;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VendaFormController {

    @FXML private Label titleLabel;
    @FXML private ComboBox<Cliente> clienteCombo;
    @FXML private DatePicker dataPicker;
    @FXML private ComboBox<Equipamento> equipamentoCombo;
    @FXML private TextField quantidadeField;
    @FXML private ListView<ItemVenda> itensListView;
    @FXML private Label totalLabel;

    private final ClienteService clienteService = new ClienteService();
    private final EquipamentoService equipamentoService = new EquipamentoService();
    private final VendaFacade vendaFacade = new VendaFacade();

    private final ObservableList<ItemVenda> itens = FXCollections.observableArrayList();

    private Venda editando;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        clienteCombo.setItems(FXCollections.observableArrayList(clienteService.listar()));
        equipamentoCombo.setItems(FXCollections.observableArrayList(equipamentoService.listar()));

        clienteCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNome());
            }
        });
        clienteCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Cliente c, boolean empty) {
                super.updateItem(c, empty);
                setText(empty || c == null ? null : c.getNome());
            }
        });

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

        itensListView.setItems(itens);
        itensListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ItemVenda item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);

                String nomeEquipamento = item.getEquipamento() == null
                        ? ""
                        : item.getEquipamento().getNome();

                Label nome = new Label(nomeEquipamento + "  ×  " + item.getQuantidade());
                nome.getStyleClass().add("list-row-title");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label subtotal = new Label(FormatUtil.currency(item.calcularSubtotal().doubleValue()));
                subtotal.getStyleClass().add("receipt-value");

                Button removerBtn = new Button("✕");
                removerBtn.getStyleClass().add("btn-icon-delete");
                removerBtn.setOnAction(e -> itens.remove(item));

                row.getChildren().addAll(nome, spacer, subtotal, removerBtn);
                setGraphic(row);
                setText(null);
            }
        });

        itens.addListener((ListChangeListener<ItemVenda>) c -> atualizarTotal());
        atualizarTotal();
    }

    public void iniciar(Venda existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Nova Venda");
        } else {
            titleLabel.setText("Editar Venda");
            clienteCombo.setValue(existente.getCliente());
            dataPicker.setValue(existente.getDataVenda());
            itens.setAll(existente.getItensVenda());
        }

        atualizarTotal();
    }

    @FXML
    private void onAdicionarItem() {
        Equipamento equipamento = equipamentoCombo.getValue();
        int quantidade = FormatUtil.parseIntOrZero(quantidadeField.getText());

        if (equipamento == null) {
            AlertUtil.erro("Selecione um equipamento.");
            return;
        }

        if (quantidade <= 0) {
            AlertUtil.erro("Informe uma quantidade válida.");
            return;
        }

        if (quantidade > equipamento.getQuantidadeEstoque()) {
            AlertUtil.erro("Estoque insuficiente para \"" + equipamento.getNome()
                    + "\". Disponível: " + equipamento.getQuantidadeEstoque() + " unidade(s).");
            return;
        }

        ItemVenda item = new ItemVenda(
                quantidade,
                BigDecimal.valueOf(equipamento.getPreco()),
                equipamento
        );

        itens.add(item);
        equipamentoCombo.setValue(null);
        quantidadeField.clear();
    }

    private void atualizarTotal() {
        BigDecimal total = itens.stream()
                .map(ItemVenda::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalLabel.setText("Total: " + FormatUtil.currency(total.doubleValue()));
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
        try {
            Cliente cliente = clienteCombo.getValue();
            LocalDate data = dataPicker.getValue();

            if (cliente == null) {
                AlertUtil.erro("Selecione o cliente.");
                return;
            }

            if (data == null) {
                AlertUtil.erro("Selecione a data da venda.");
                return;
            }

            if (itens.isEmpty()) {
                AlertUtil.erro("Adicione ao menos um item à venda.");
                return;
            }

            if (editando == null) {
                Venda nova = new Venda(0, cliente);
                nova.setDataVenda(data);

                for (ItemVenda item : itens) {
                    nova.adicionarItemDeVenda(item);
                }

                vendaFacade.realizarVenda(nova);
            } else {
                // Para editar de verdade pelo Facade seria ideal criar um método específico.
                // Por enquanto: cancela a venda antiga e registra uma nova com os dados atualizados.
                vendaFacade.cancelarVenda(editando);

                Venda nova = new Venda(0, cliente);
                nova.setDataVenda(data);

                for (ItemVenda item : itens) {
                    nova.adicionarItemDeVenda(item);
                }

                vendaFacade.realizarVenda(nova);
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
