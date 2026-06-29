package com.mycompany.undertale_3.controller;

import javafx.scene.media.*;
import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.JogadorDAO;
import com.mycompany.undertale_3.dao.UpgradeDAO;
import com.mycompany.undertale_3.model.Jogador;
import com.mycompany.undertale_3.util.GameSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.util.Map;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuController {

    @FXML private TextField nomeField;
    @FXML private ImageView logo;

    private Media media;
    private MediaPlayer player;

    @FXML
    void initialize() {
        try {
            String caminho = getClass()
                    .getResource("/com/mycompany/undertale_3/Música 1 UNDERTALE Toby Fox.m4a")
                    .toString();

            media = new Media(caminho);
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.play();

        } catch (Exception e) {
            System.out.println("Erro ao carregar música do menu: " + e.getMessage());
        }

        logo.setImage(img("logo-jogo.PNG"));
    }

    private Image img(String nome) {
        return new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/" + nome));
    }

    private void pararMusica() {
        try {
            if (player != null) {
                player.stop();
                player.dispose();
                player = null;
            }
        } catch (Exception e) {
            System.out.println("Erro ao parar música do menu.");
        }
    }

    @FXML
    void jogar() {
        String nome = nomeField.getText() == null ? "" : nomeField.getText().trim();

        if (nome.isEmpty()) {
            alerta("Digite o nome do jogador.");
            return;
        }

        Jogador jogador = new JogadorDAO().buscarOuCriar(nome);

        if (jogador == null) {
            alerta("Não foi possível criar/buscar jogador. Confira o PostgreSQL.");
            return;
        }

        Map<String, Integer> ups = new UpgradeDAO().listar(jogador.getId());
        GameSession.novaRun(jogador, ups.get("hp"), ups.get("atk"), ups.get("def"));

        pararMusica();
        App.trocarTela("jogo.fxml", "Undertale 3 - Gameplay");
    }

    @FXML
    void ranking() {
        pararMusica();
        App.trocarTela("ranking.fxml", "Ranking");
    }

    @FXML
    void creditos() {
        pararMusica();
        App.trocarTela("creditos.fxml", "Créditos");
    }

    @FXML
    void encerrar() {
        pararMusica();
        App.getPrimaryStage().close();
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}