package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Equipamento;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.EquipamentoService;
import br.edu.ufersa.minhaCasaClandestinaTech.util.AlertUtil;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import br.edu.ufersa.minhaCasaClandestinaTech.model.strategy.EstrategiaBusca;
import br.edu.ufersa.minhaCasaClandestinaTech.model.strategy.EstrategiasBusca;

public class EquipamentoController {

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
    @FXML private TableColumn<Equipamento, Void> colAcoes;

    private final EquipamentoService equipamentoService = new EquipamentoService();
    private FilteredList<Equipamento> filteredList;

    private EstrategiaBusca estrategiaAtual = EstrategiasBusca.POR_NOME;

    @FXML
    public void initialize() {
        ToggleGroup grupo = new ToggleGroup();
        filtroNome.setToggleGroup(grupo);
        filtroSerie.setToggleGroup(grupo);
        filtroResponsavel.setToggleGroup(grupo);
        filtroLocal.setToggleGroup(grupo);
        filtroNome.setSelected(true);

        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNome()));

        colSerie.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getNumeroSerie())));

        colPreco.setCellValueFactory(c ->
                new SimpleStringProperty(FormatUtil.currency(c.getValue().getPreco())));

        colQtd.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getQuantidadeEstoque())));

        // Sua entidade Equipamento atual não possui Local nem Responsável.
        colLocal.setCellValueFactory(c -> {
            var l = c.getValue().getLocal();
            return new SimpleStringProperty(l != null ? l.getNomeCasa() + " / " + l.getNomeCompartimento() : "-");
        });
        colResponsavel.setCellValueFactory(c -> {
            var r = c.getValue().getResponsavel();
            return new SimpleStringProperty(r != null ? r.getNome() : "-");
        });

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

        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("✎");
            private final Button delBtn = new Button("🗑");
            private final HBox box = new HBox(4, editBtn, delBtn);

            {
                editBtn.getStyleClass().add("btn-icon-edit");
                delBtn.getStyleClass().add("btn-icon-delete");
                box.setAlignment(Pos.CENTER_LEFT);

                editBtn.setOnAction(e -> {
                    Equipamento equipamento = getTableView().getItems().get(getIndex());
                    abrirModal(equipamento);
                });

                delBtn.setOnAction(e -> {
                    Equipamento equipamento = getTableView().getItems().get(getIndex());
                    excluir(equipamento);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        carregarEquipamentos();

        searchField.textProperty().addListener((obs, oldV, newV) -> aplicarFiltro());

        grupo.selectedToggleProperty().addListener((obs, oldV, newV) -> {
            estrategiaAtual = resolverEstrategia();
            aplicarFiltro();
        });
    }

    private void carregarEquipamentos() {
        filteredList = new FilteredList<>(
                FXCollections.observableArrayList(equipamentoService.listar()),
                e -> true
        );

        tabela.setItems(filteredList);
        atualizarSubtitulo();
    }

    // DEPOIS
    private EstrategiaBusca resolverEstrategia() {
        if (filtroSerie.isSelected())       return EstrategiasBusca.POR_SERIE;
        if (filtroResponsavel.isSelected()) return EstrategiasBusca.POR_RESPONSAVEL;
        if (filtroLocal.isSelected())       return EstrategiasBusca.POR_LOCAL;
        return EstrategiasBusca.POR_NOME;
    }

    private void aplicarFiltro() {
        String termo = searchField.getText() == null
                ? ""
                : searchField.getText().trim().toLowerCase();

        filteredList.setPredicate(eq -> {
            if (termo.isEmpty()) return true;
            return estrategiaAtual.corresponde(eq, termo);
        });

        atualizarSubtitulo();
    }

    private void atualizarSubtitulo() {
        int total = filteredList == null ? 0 : filteredList.size();

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

        controller.iniciar(existente, () -> {
            carregarEquipamentos();
            AppContext.getMainController().refreshCurrentView();
        });
    }

    private void excluir(Equipamento equipamento) {
        if (equipamento == null) {
            return;
        }

        boolean ok = AlertUtil.confirmar(
                "Excluir equipamento",
                "Tem certeza que deseja excluir \"" + equipamento.getNome() + "\"?"
        );

        if (ok) {
            try {
                equipamentoService.deletar(equipamento);
                carregarEquipamentos();
            } catch (RuntimeException e) {
                AlertUtil.erro(e.getMessage());
            }
        }
    }
}
