package com.minhacasa.estoque;

import com.minhacasa.estoque.controller.MainController;

/**
 * Pequeno "service locator" para permitir que telas/controllers carregados
 * dinamicamente (views e modais) consigam acessar o MainController e
 * disparar acoes como abrir modal, fechar modal ou atualizar a tela atual.
 */
public final class AppContext {

    private static MainController mainController;

    private AppContext() {
    }

    public static void setMainController(MainController controller) {
        mainController = controller;
    }

    public static MainController getMainController() {
        return mainController;
    }
}
