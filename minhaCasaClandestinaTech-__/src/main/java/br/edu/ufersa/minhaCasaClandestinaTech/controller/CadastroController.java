package br.edu.ufersa.minhaCasaClandestinaTech.controller;

import br.edu.ufersa.minhaCasaClandestinaTech.Main;
import br.edu.ufersa.minhaCasaClandestinaTech.model.entities.Usuario;
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

public class CadastroController {

    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;
    @FXML private Label mensagemLabel;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void onCadastrar() {
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

        try {
            Usuario usuario = new Usuario(0,email, senha);
            usuarioService.cadastrar(usuario);
            mostrarSucesso("Conta criada com sucesso! Faça o login.");
            emailField.clear();
            senhaField.clear();
        } catch (RuntimeException e) {
            mostrarErro(e.getMessage());
        }
    }

    @FXML
    private void onVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                    Main.class.getResource(
                            "/br/edu/ufersa/minhaCasaClandestinaTech/fxml/login.fxml")
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
            mostrarErro("Erro ao voltar para o login.");
            e.printStackTrace();
        }
    }

    private void mostrarErro(String mensagem) {
        mensagemLabel.setText(mensagem);
        mensagemLabel.setStyle("-fx-text-fill: #e53935;");
        mensagemLabel.setVisible(true);
        mensagemLabel.setManaged(true);
    }

    private void mostrarSucesso(String mensagem) {
        mensagemLabel.setText(mensagem);
        mensagemLabel.setStyle("-fx-text-fill: #2e7d32;");
        mensagemLabel.setVisible(true);
        mensagemLabel.setManaged(true);
    }
}
