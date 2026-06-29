package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.UpgradeDAO;
import com.mycompany.undertale_3.util.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class UpgradesController {

    @FXML private Label pontosLabel;

    private final UpgradeDAO dao = new UpgradeDAO();

    @FXML
    void initialize() {
        atualizar();
    }

    @FXML
    void comprarHp() {
        comprar("hp", 120);
    }

    @FXML
    void comprarAtk() {
        comprar("atk", 100);
    }

    @FXML
    void comprarDef() {
        comprar("def", 110);
    }

    private void comprar(String tipo, int custo) {
        if (GameSession.pontosRun < custo) {
            new Alert(Alert.AlertType.WARNING, "Pontos insuficientes.").showAndWait();
            return;
        }

        GameSession.pontosRun -= custo;

        switch (tipo) {
            case "hp" -> {
                GameSession.maxHp += 40;
                GameSession.hp += 40;
            }
            case "atk" -> GameSession.atk += 5;
            case "def" -> GameSession.def += 3;
        }

        dao.comprar(GameSession.jogador.getId(), tipo);

        atualizar();

        new Alert(Alert.AlertType.INFORMATION, "Upgrade comprado!").showAndWait();
    }

    private void atualizar() {
        pontosLabel.setText(
                "Pontos da run: " + GameSession.pontosRun
                + " | HP: " + GameSession.hp + "/" + GameSession.maxHp
                + " | ATK: " + GameSession.atk
                + " | DEF: " + GameSession.def
        );
    }

    @FXML
    void voltar() {
        App.trocarTela("jogo.fxml", "Gameplay");
    }
}