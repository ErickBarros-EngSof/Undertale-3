package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class FinalFelizController {

    @FXML
    private ImageView imagemFinal;

    @FXML
    private StackPane root;

    @FXML
    private ImageView FinalFelizImage;

    @FXML
    private Button botaoTelaRanking;

    @FXML
    public void initialize() {

        FinalFelizImage.setImage(new Image(
                getClass().getResourceAsStream("/com/mycompany/undertale_3/FinalFeliz.png")));

        FinalFelizImage.fitWidthProperty().bind(root.widthProperty());
        FinalFelizImage.fitHeightProperty().bind(root.heightProperty());
        FinalFelizImage.setPreserveRatio(false);
    }

    @FXML
    private void ranking() {
        App.trocarTela("ranking.fxml", "Ranking");
    }
}