package com.mycompany.undertale_3.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Random;

public class CombatMenuController {

    @FXML private Label inimigoLabel;
    @FXML private Label mensagemLabel;
    @FXML private ImageView inimigo1;

    private JogoController gameController;
    private String inimigo;
    private Stage stage;

    @FXML
    public void initialize() {
        inimigo1.setImage(new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/inimigo.png")));
    }

    public void receberDados(JogoController gameController, String inimigo, Stage stage) {
        this.gameController = gameController;
        this.inimigo = inimigo;
        this.stage = stage;
        inimigoLabel.setText(inimigo);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void lutar() {
        gameController.abrirFight(inimigo, stage);
    }

    @FXML
    private void conversar() {
        if (new Random().nextBoolean()) {
            mensagemLabel.setText("O inimigo aceitou conversar. Você ganhou XP!");
            gameController.ganharXP(15);
        } else {
            mensagemLabel.setText("O inimigo rejeitou sua frase. Você levou dano!");
            gameController.levarDano(15);
        }
    }

    @FXML
    private void fugir() {
        if (new Random().nextInt(100) < 50) {
            mensagemLabel.setText("Você conseguiu fugir!");
            if (stage != null) stage.close();
        } else {
            mensagemLabel.setText("Você tentou fugir, mas levou dano!");
            gameController.levarDano(10);
        }
    }
}

