package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class GameOverController {

    @FXML
    private StackPane root;

    @FXML
    private ImageView gameOverImage;

    @FXML
    private Button botaoTelaInicial;

    @FXML
    public void initialize() {
        gameOverImage.setImage(new Image(
                getClass().getResourceAsStream("/com/mycompany/undertale_3/GameOver.png")
        ));

        gameOverImage.fitWidthProperty().bind(root.widthProperty());
        gameOverImage.fitHeightProperty().bind(root.heightProperty());
        gameOverImage.setPreserveRatio(false);
    }

    @FXML
    private void voltarTelaInicial() {
        App.trocarTela("menu.fxml", "Menu");
    }
}