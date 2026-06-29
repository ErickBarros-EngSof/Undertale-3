package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.JogadorDAO;
import com.mycompany.undertale_3.dao.RunDAO;
import com.mycompany.undertale_3.util.GameSession;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BossController {

    @FXML private Label hpPlayerLabel;
    @FXML private Label hpBossLabel;
    @FXML private Label msgLabel;
    @FXML private ImageView inimigo;

    private final int hpBossMax = 1000;
    private int hpBoss = hpBossMax;

    private final Random random = new Random();

    @FXML
    void initialize() {
        inimigo.setImage(new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/boss.png")));
        atualizar();
    }

    @FXML
    void atacar() {
        int danoPlayer = GameSession.atk + 20 + random.nextInt(20);
        hpBoss -= danoPlayer;

        if (hpBoss <= 0) {
            vencer();
            return;
        }

        int danoBoss = Math.max(18, 55 - GameSession.def + random.nextInt(20));
        GameSession.hp -= danoBoss;

        msgLabel.setText("Você causou " + danoPlayer + " de dano. O Boss causou " + danoBoss + ".");

        if (GameSession.hp <= 0) {
            GameSession.hp = 0;
            perder();
        } else {
            atualizar();
        }
    }

    @FXML
    void usarPocao() {
        GameSession.hp = Math.min(GameSession.maxHp, GameSession.hp + 40);

        int danoBoss = Math.max(15, 45 - GameSession.def + random.nextInt(15));
        GameSession.hp -= danoBoss;

        msgLabel.setText("Você recuperou 40 HP, mas o Boss atacou e causou " + danoBoss + ".");

        if (GameSession.hp <= 0) {
            GameSession.hp = 0;
            perder();
        } else {
            atualizar();
        }
    }

    private void vencer() {

        GameSession.pontosRun += 1000;
        GameSession.moedasRun += 500;

        salvar(true);

        App.trocarTela("FinalFeliz.fxml", "Final Feliz");
    }

    private void perder() {
        salvar(false);
        App.trocarTela("GameOver.fxml", "Derrota");
    }

    private void salvar(boolean venceu) {
        new RunDAO().salvarRun(
                GameSession.jogador.getId(),
                GameSession.pontosRun,
                GameSession.moedasRun,
                venceu
        );

        new JogadorDAO().somarResultado(
                GameSession.jogador.getId(),
                GameSession.pontosRun,
                GameSession.moedasRun
        );
    }

    private void atualizar() {
        hpPlayerLabel.setText("Seu HP: " + GameSession.hp + "/" + GameSession.maxHp);
        hpBossLabel.setText("HP Boss: " + Math.max(0, hpBoss) + "/" + hpBossMax);
    }
}
