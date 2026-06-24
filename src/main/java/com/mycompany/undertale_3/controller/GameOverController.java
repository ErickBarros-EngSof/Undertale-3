package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class GameOverController {

    @FXML
    private Button botaoTelaInicial;

    @FXML
    private void voltarTelaInicial() {

        try {
            App.trocarTela("menu.fxml", "Menu");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    }
