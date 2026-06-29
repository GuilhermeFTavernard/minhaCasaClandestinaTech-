package br.edu.ufersa.minhaCasaClandestinaTech.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/** Helpers para exibir alertas de confirmacao, erro e validacao. */
public final class AlertUtil {

    private AlertUtil() {
    }

    public static void erro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alert.setHeaderText("Verifique os campos");
        alert.setTitle("Erro de validacao");
        alert.showAndWait();
    }

    public static boolean confirmar(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(titulo);
        alert.setTitle(titulo);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
}
