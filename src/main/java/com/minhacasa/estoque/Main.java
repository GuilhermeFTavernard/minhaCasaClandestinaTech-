package com.minhacasa.estoque;

import com.minhacasa.estoque.data.DataStore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Ponto de entrada da aplicacao JavaFX.
 * Sistema de controle de estoque - minhaCasaClandestinaTech
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Inicializa a base de dados em memoria (com dados de exemplo)
        DataStore.getInstance();

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                Main.class.getResource("/com/minhacasa/estoque/fxml/main.fxml"),
                "main.fxml nao encontrado"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1366, 800);
        scene.getStylesheets().add(Objects.requireNonNull(
                Main.class.getResource("/com/minhacasa/estoque/css/styles.css"),
                "styles.css nao encontrado").toExternalForm());

        primaryStage.setTitle("Sistema de controle de estoque - minhaCasaClandestinaTech");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1150);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
