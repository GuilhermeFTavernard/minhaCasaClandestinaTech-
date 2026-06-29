package br.edu.ufersa.minhaCasaClandestinaTech;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(
                Main.class.getResource(
                        "/br/edu/ufersa/minhaCasaClandestinaTech/fxml/login.fxml")
        ));

        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);

        scene.getStylesheets().add(Objects.requireNonNull(
                Main.class.getResource(
                        "/br/edu/ufersa/minhaCasaClandestinaTech/css/styles.css")
        ).toExternalForm());

        primaryStage.setTitle("minhaCasaClandestinaTech");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}