package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.Main;
import br.edu.ufersa.minhaCasaClandestinaTech.model.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Label erroLabel;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void onLogin() {
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String senha = senhaField.getText() == null ? "" : senhaField.getText().trim();

        if (email.isEmpty()) {
            mostrarErro("Informe o e-mail.");
            return;
        }

        if (senha.isEmpty()) {
            mostrarErro("Informe a senha.");
            return;
        }

        boolean autenticado = usuarioService.logar(email, senha);

        if (autenticado) {
            abrirTelaPrincipal();
        } else {
            mostrarErro("E-mail ou senha incorretos.");
        }
    }

    @FXML
    private void onCadastrar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    Main.class.getResource(
                            "/br/edu/ufersa/minhaCasaClandestinaTech/fxml/cadastro.fxml")
            ));

            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);

            scene.getStylesheets().add(Objects.requireNonNull(
                    Main.class.getResource(
                            "/br/edu/ufersa/minhaCasaClandestinaTech/css/styles.css")
            ).toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            mostrarErro("Erro ao abrir o cadastro.");
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        erroLabel.setText(mensagem);
        erroLabel.setVisible(true);
        erroLabel.setManaged(true);
    }

    private void abrirTelaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    Main.class.getResource(
                            "/br/edu/ufersa/minhaCasaClandestinaTech/fxml/main.fxml")
            ));

            Parent root = loader.load();

            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root, 1366, 800);

            scene.getStylesheets().add(Objects.requireNonNull(
                    Main.class.getResource(
                            "/br/edu/ufersa/minhaCasaClandestinaTech/css/styles.css")
            ).toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            mostrarErro("Erro ao abrir o sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}