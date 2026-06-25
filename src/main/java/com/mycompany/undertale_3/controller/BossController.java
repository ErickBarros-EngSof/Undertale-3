package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.JogadorDAO;
import com.mycompany.undertale_3.dao.RunDAO;
import com.mycompany.undertale_3.util.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BossController {
    @FXML private Label hpPlayerLabel, hpBossLabel, msgLabel;
    private int hpBoss = 220;
    private final Random random = new Random();
    @FXML private ImageView inimigo;

    @FXML void initialize() { 
        atualizar(); 
        inimigo.setImage(new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/boss.png")));
    }

    @FXML void atacar() {
        int dano = GameSession.atk + 15 + random.nextInt(15);
        hpBoss -= dano;
        if (hpBoss <= 0) { vencer(); return; }
        int danoBoss = Math.max(8, 32 - GameSession.def + random.nextInt(12));
        GameSession.hp -= danoBoss;
        msgLabel.setText("Você causou " + dano + ". O boss causou " + danoBoss + ".");
        if (GameSession.hp <= 0) perder(); else atualizar();
    }

    @FXML void usarPocao() {
        GameSession.hp = Math.min(GameSession.maxHp, GameSession.hp + 40);
        msgLabel.setText("Você recuperou 40 HP.");
        atualizar();
    }

    private void vencer() {
        GameSession.pontosRun += 500;
        GameSession.moedasRun += 200;
        salvar(true);
        App.trocarTela("ranking.fxml", "Você venceu o Boss");
    }

    private void perder() { salvar(false); App.trocarTela("ranking.fxml", "Derrota"); }

    private void salvar(boolean venceu) {
        new RunDAO().salvarRun(GameSession.jogador.getId(), GameSession.pontosRun, GameSession.moedasRun, venceu);
        new JogadorDAO().somarResultado(GameSession.jogador.getId(), GameSession.pontosRun, GameSession.moedasRun);
    }

    private void atualizar() {
        hpPlayerLabel.setText("Seu HP: " + GameSession.hp + "/" + GameSession.maxHp);
        hpBossLabel.setText("HP Boss: " + Math.max(0, hpBoss) + "/220");
    }
}
