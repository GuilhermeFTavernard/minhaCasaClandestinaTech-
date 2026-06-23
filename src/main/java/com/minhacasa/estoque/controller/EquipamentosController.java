package com.minhacasa.estoque.controller;

import com.minhacasa.estoque.AppContext;
import com.minhacasa.estoque.data.DataStore;
import com.minhacasa.estoque.model.Equipamento;
import com.minhacasa.estoque.strategy.EstrategiaBusca;
import com.minhacasa.estoque.strategy.EstrategiasBusca;
import com.minhacasa.estoque.util.AlertUtil;
import com.minhacasa.estoque.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Listagem de equipamentos com padrão Strategy aplicado ao filtro de busca.
 *
 * Mudanças em relação à versão original:
 *  - Adicionado atributo `estrategiaAtual` do tipo EstrategiaBusca
 *  - Adicionado método `resolverEstrategia()` que mapeia o botão selecionado
 *    para a estratégia correspondente em EstrategiasBusca
 *  - `aplicarFiltro()` simplificado: delega toda lógica de filtragem
 *    para `estrategiaAtual.corresponde()`, sem if/else de critério
 */
public class EquipamentosController {

    @FXML private Label subtitleLabel;
    @FXML private TextField searchField;
    @FXML private ToggleButton filtroNome;
    @FXML private ToggleButton filtroSerie;
    @FXML private ToggleButton filtroResponsavel;
    @FXML private ToggleButton filtroLocal;

    @FXML private TableView<Equipamento> tabela;
    @FXML private TableColumn<Equipamento, String> colNome;
    @FXML private TableColumn<Equipamento, String> colSerie;
    @FXML private TableColumn<Equipamento, String> colPreco;
    @FXML private TableColumn<Equipamento, String> colQtd;
    @FXML private TableColumn<Equipamento, String> colLocal;
    @FXML private TableColumn<Equipamento, String> colResponsavel;
    @FXML private TableColumn<Equipamento, Void>   colAcoes;

    private final DataStore store = DataStore.getInstance();
    private FilteredList<Equipamento> filteredList;

    // ── Strategy ──────────────────────────────────────────────────────────────
    /** Estratégia de busca atualmente selecionada pelo usuário. */
    private EstrategiaBusca estrategiaAtual = EstrategiasBusca.POR_NOME;
    // ─────────────────────────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        // Configuração dos botões de filtro
        ToggleGroup grupo = new ToggleGroup();
        filtroNome.setToggleGroup(grupo);
        filtroSerie.setToggleGroup(grupo);
        filtroResponsavel.setToggleGroup(grupo);
        filtroLocal.setToggleGroup(grupo);
        filtroNome.setSelected(true);

        // Configuração da tabela
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));
        colSerie.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNumeroSerie()));
        colPreco.setCellValueFactory(c ->
                new SimpleStringProperty(FormatUtil.currency(c.getValue().getPreco())));
        colQtd.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getQuantidade())));
        colLocal.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getLocal() == null ? ""
                        : c.getValue().getLocal().getDescricao()));
        colResponsavel.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getResponsavel() == null ? ""
                        : c.getValue().getResponsavel().getNome()));

        // Badge de quantidade
        colQtd.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("badge-qty");
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        // Botões de ação por linha
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✎");
            private final Button delBtn  = new Button("🗑");
            private final HBox   box     = new HBox(4, editBtn, delBtn);
            {
                editBtn.getStyleClass().add("btn-icon-edit");
                delBtn.getStyleClass().add("btn-icon-delete");
                box.setAlignment(Pos.CENTER_LEFT);
                editBtn.setOnAction(e -> abrirModal(getTableRow().getItem()));
                delBtn.setOnAction(e -> excluir(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        // Lista filtrada ligada à tabela
        filteredList = new FilteredList<>(store.getEquipamentos(), e -> true);
        tabela.setItems(filteredList);

        // Listeners: qualquer mudança no campo ou no botão reaplica o filtro
        searchField.textProperty().addListener((obs, oldV, newV) -> aplicarFiltro());
        grupo.selectedToggleProperty().addListener((obs, oldV, newV) -> {
            estrategiaAtual = resolverEstrategia(); // ← troca a estratégia
            aplicarFiltro();
        });

        atualizarSubtitulo();
    }

    // ── Strategy: resolve qual estratégia usar conforme o botão ativo ────────

    /**
     * Mapeia o ToggleButton selecionado para a estratégia correspondente.
     * Para adicionar um novo critério, basta incluir um else-if aqui
     * e criar a lambda em EstrategiasBusca — o método aplicarFiltro()
     * não precisa ser tocado.
     */
    private EstrategiaBusca resolverEstrategia() {
        if (filtroSerie.isSelected())       return EstrategiasBusca.POR_SERIE;
        if (filtroResponsavel.isSelected()) return EstrategiasBusca.POR_RESPONSAVEL;
        if (filtroLocal.isSelected())       return EstrategiasBusca.POR_LOCAL;
        return EstrategiasBusca.POR_NOME;   // padrão
    }

    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Aplica o predicado de filtragem usando a estratégia atual.
     * Este método não sabe (nem precisa saber) qual critério está ativo —
     * ele apenas delega para estrategiaAtual.corresponde().
     */
    private void aplicarFiltro() {
        String termo = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        filteredList.setPredicate(eq -> {
            if (termo.isEmpty()) return true;
            return estrategiaAtual.corresponde(eq, termo); // ← delegação total
        });
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void atualizarSubtitulo() {
        int total = store.getEquipamentos().size();
        subtitleLabel.setText(total
                + (total == 1 ? " equipamento cadastrado" : " equipamentos cadastrados"));
    }

    @FXML
    private void onNovo() {
        abrirModal(null);
    }

    private void abrirModal(Equipamento existente) {
        EquipamentoFormController controller =
                AppContext.getMainController().showModal("equipamento_form.fxml");
        controller.iniciar(existente,
                () -> AppContext.getMainController().refreshCurrentView());
    }

    private void excluir(Equipamento equipamento) {
        if (equipamento == null) return;
        boolean ok = AlertUtil.confirmar("Excluir equipamento",
                "Tem certeza que deseja excluir \"" + equipamento.getNome() + "\"?");
        if (ok) {
            store.getEquipamentos().remove(equipamento);
            AppContext.getMainController().refreshCurrentView();
        }
    }
}
