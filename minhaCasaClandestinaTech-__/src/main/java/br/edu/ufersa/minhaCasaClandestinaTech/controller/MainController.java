package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.AppContext;
import br.edu.ufersa.minhaCasaClandestinaTech.util.FormatUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class MainController {

    private static final String FXML_BASE =
            "/br/edu/ufersa/minhaCasaClandestinaTech/fxml/";

    @FXML private StackPane contentArea;
    @FXML private StackPane modalOverlay;
    @FXML private StackPane modalContainer;

    @FXML private Label pageTitleLabel;
    @FXML private Label dateLabel;

    @FXML private Button btnDashboard;
    @FXML private Button btnEquipamentos;
    @FXML private Button btnLocais;
    @FXML private Button btnResponsaveis;
    @FXML private Button btnClientes;
    @FXML private Button btnVendas;
    @FXML private Button btnCompras;
    @FXML private Button btnRelatorios;

    private Button currentActiveButton;
    private String currentFxml;
    private String currentTitle;

    @FXML
    public void initialize() {
        AppContext.setMainController(this);
        dateLabel.setText(FormatUtil.dateLong(LocalDate.now()));
        onDashboard();
    }

    @FXML public void onDashboard()    { loadView("dashboard.fxml", btnDashboard, "Dashboard"); }
    @FXML public void onEquipamentos() { loadView("equipamentos.fxml", btnEquipamentos, "Equipamentos"); }
    @FXML public void onLocais()       { loadView("locais.fxml", btnLocais, "Locais"); }
    @FXML public void onResponsaveis() { loadView("responsaveis.fxml", btnResponsaveis, "Responsáveis"); }
    @FXML public void onClientes()     { loadView("clientes.fxml", btnClientes, "Clientes"); }
    @FXML public void onVendas()       { loadView("vendas.fxml", btnVendas, "Vendas"); }
    @FXML public void onCompras()      { loadView("compras.fxml", btnCompras, "Compras"); }
    @FXML public void onRelatorios()   { loadView("relatorios.fxml", btnRelatorios, "Relatórios"); }

    private void loadView(String fxml, Button activeButton, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(
                            getClass().getResource(FXML_BASE + fxml)
                    )
            );

            Parent view = loader.load();

            contentArea.getChildren().setAll(view);
            setActiveButton(activeButton);

            pageTitleLabel.setText(title);
            currentFxml = fxml;
            currentTitle = title;

        } catch (IOException e) {
            throw new RuntimeException("Falha ao carregar a tela: " + fxml, e);
        }
    }

    private void setActiveButton(Button button) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("menu-active");
        }

        button.getStyleClass().add("menu-active");
        currentActiveButton = button;
    }

    public <T> T showModal(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(
                            getClass().getResource(FXML_BASE + fxmlPath)
                    )
            );

            Parent content = loader.load();

            modalContainer.getChildren().setAll(content);
            modalOverlay.setVisible(true);
            modalOverlay.setManaged(true);

            return loader.getController();

        } catch (IOException e) {
            throw new RuntimeException("Falha ao abrir o modal: " + fxmlPath, e);
        }
    }

    public void hideModal() {
        modalOverlay.setVisible(false);
        modalOverlay.setManaged(false);
        modalContainer.getChildren().clear();
    }

    @FXML
    private void onOverlayClicked(MouseEvent event) {
        if (event.getTarget() == modalOverlay) {
            hideModal();
        }
    }

    public void refreshCurrentView() {
        if (currentFxml != null) {
            loadView(currentFxml, currentActiveButton, currentTitle);
        }
    }
}