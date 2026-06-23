package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Cliente;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.model.ItemVenda;
import com.minhacasa.estoque.model.StatusVenda;
import com.minhacasa.estoque.model.Venda;
import com.minhacasa.estoque.util.AlertUtil;
import com.minhacasa.estoque.util.FormatUtil;
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

import java.time.LocalDate;

/**
 * Controller do modal "Nova/Editar Venda". Nao havia mockup especifico desta
 * tela no protototipo do Figma, entao o layout foi inferido a partir do
 * padrao visual dos demais formularios (modal-card) somado a uma secao para
 * adicionar/remover itens, ja que uma venda pode ter varios equipamentos.
 *
 * Ao salvar, os itens vendidos sao automaticamente baixados do estoque dos
 * respectivos equipamentos (e devolvidos caso a venda seja editada novamente).
 */
public class VendaFormController {

    @FXML private Label titleLabel;
    @FXML private ComboBox<Cliente> clienteCombo;
    @FXML private DatePicker dataPicker;
    @FXML private ComboBox<Equipamento> equipamentoCombo;
    @FXML private TextField quantidadeField;
    @FXML private ListView<ItemVenda> itensListView;
    @FXML private Label totalLabel;

    private final DataStore store = DataStore.getInstance();
    private final ObservableList<ItemVenda> itens = FXCollections.observableArrayList();

    private Venda editando;
    private Runnable onSaved;

    @FXML
    public void initialize() {
        clienteCombo.setItems(store.getClientes());
        equipamentoCombo.setItems(store.getEquipamentos());
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

                Label nome = new Label(item.getEquipamento().getNome() + "  ×  " + item.getQuantidade());
                nome.getStyleClass().add("list-row-title");

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label subtotal = new Label(FormatUtil.currency(item.getSubtotal()));
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

    /**
     * @param existente venda a editar, ou null para criar uma nova venda
     * @param onSaved   callback executado apos salvar (atualiza a tabela de vendas)
     */
    public void iniciar(Venda existente, Runnable onSaved) {
        this.editando = existente;
        this.onSaved = onSaved;

        if (existente == null) {
            titleLabel.setText("Nova Venda");
        } else {
            titleLabel.setText("Editar Venda");
            clienteCombo.setValue(existente.getCliente());
            dataPicker.setValue(existente.getData());
            itens.setAll(existente.getItens());
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
        // Validacao de estoque disponivel apenas para vendas novas. Em edicao,
        // a conferencia final ocorre no momento de salvar (estoque antigo eh
        // devolvido antes da nova baixa).
        if (editando == null && quantidade > equipamento.getQuantidade()) {
            AlertUtil.erro("Estoque insuficiente para \"" + equipamento.getNome()
                    + "\". Disponível: " + equipamento.getQuantidade() + " unidade(s).");
            return;
        }

        itens.add(new ItemVenda(equipamento, quantidade, equipamento.getPreco()));
        equipamentoCombo.setValue(null);
        quantidadeField.clear();
    }

    private void atualizarTotal() {
        double total = itens.stream().mapToDouble(ItemVenda::getSubtotal).sum();
        totalLabel.setText("Total: " + FormatUtil.currency(total));
    }

    @FXML
    private void onCancelar() {
        AppContext.getMainController().hideModal();
    }

    @FXML
    private void onSalvar() {
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
            Venda nova = new Venda(store.nextNumeroVenda(), data, cliente, StatusVenda.ATIVA);
            nova.getItens().addAll(itens);
            store.getVendas().add(0, nova);
            baixarEstoque(itens);
        } else {
            // devolve o estoque referente aos itens antigos antes de aplicar os novos
            devolverEstoque(editando.getItens());
            editando.setCliente(cliente);
            editando.setData(data);
            editando.getItens().setAll(itens);
            baixarEstoque(itens);
        }

        if (onSaved != null) {
            onSaved.run();
        }
        AppContext.getMainController().hideModal();
    }

    private void baixarEstoque(java.util.List<ItemVenda> lista) {
        for (ItemVenda item : lista) {
            Equipamento eq = item.getEquipamento();
            if (eq != null) {
                eq.setQuantidade(eq.getQuantidade() - item.getQuantidade());
            }
        }
    }

    private void devolverEstoque(java.util.List<ItemVenda> lista) {
        for (ItemVenda item : lista) {
            Equipamento eq = item.getEquipamento();
            if (eq != null) {
                eq.setQuantidade(eq.getQuantidade() + item.getQuantidade());
            }
        }
    }
}
