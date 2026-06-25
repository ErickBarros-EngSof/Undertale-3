package com.mycompany.undertale_3.controller;

import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.JogadorDAO;
import com.mycompany.undertale_3.dao.RunDAO;
import com.mycompany.undertale_3.util.GameSession;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class Jogo2Controller {

    private Media media;
    private MediaPlayer playerv;

    @FXML private Pane mapaPane;

    @FXML private Label nomeLabel;
    @FXML private Label hpLabel;
    @FXML private Label moedasLabel;
    @FXML private Label pontosLabel;
    @FXML private Label msgLabel;

    @FXML private ImageView player;
    @FXML private ImageView inimigo1;
    @FXML private ImageView inimigo2;
    @FXML private ImageView inimigo3;
    @FXML private ImageView inimigo4;
    @FXML private ImageView inimigo5;

    @FXML private ImageView bau1;
    @FXML private ImageView bau2;
    @FXML private ImageView bau3;

    @FXML private ImageView bossPortal;

    @FXML private Button bossBtn;

    @FXML
    void initialize() {
        iniciarMusica();
        atualizarHud();

        player.setImage(img("personagem.png"));

        inimigo1.setImage(img("inimigo.png"));
        inimigo2.setImage(img("inimigo.png"));
        inimigo3.setImage(img("inimigo.png"));
        inimigo4.setImage(img("inimigo.png"));
        inimigo5.setImage(img("inimigo.png"));

        bau1.setImage(img("bau.png"));
        bau2.setImage(img("bau.png"));
        bau3.setImage(img("bau.png"));

        bossPortal.setImage(img("portal.png"));
        bossPortal.setVisible(false);

        bossBtn.setDisable(true);

        Platform.runLater(() -> mapaPane.requestFocus());
    }

    private void iniciarMusica() {
        try {
            URL url = getClass().getResource("/com/mycompany/undertale_3/Música 2 UNDERTALE Toby Fox.m4a");

            if (url != null) {
                media = new Media(url.toString());
                playerv = new MediaPlayer(media);
                playerv.setCycleCount(MediaPlayer.INDEFINITE);
                playerv.play();
            } else {
                System.out.println("Música não encontrada.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar música: " + e.getMessage());
        }
    }

    private void pararMusica() {
        if (playerv != null) {
            playerv.stop();
            playerv.dispose();
        }
    }

    private Image img(String nome) {
        return new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/" + nome));
    }

    @FXML
    private void teclaPressionada(KeyEvent event) {
        switch (event.getCode()) {
            case W -> mover(0, -18);
            case S -> mover(0, 18);
            case A -> mover(-18, 0);
            case D -> mover(18, 0);
            default -> { }
        }
    }

    private void mover(double dx, double dy) {
        double novoX = player.getLayoutX() + dx;
        double novoY = player.getLayoutY() + dy;

        player.setLayoutX(Math.max(20, Math.min(820, novoX)));
        player.setLayoutY(Math.max(70, Math.min(470, novoY)));

        verificarColisoes();
    }

    private void verificarColisoes() {
        verificarBau(bau1);
        verificarBau(bau2);
        verificarBau(bau3);

        verificarInimigo(inimigo1);
        verificarInimigo(inimigo2);
        verificarInimigo(inimigo3);
        verificarInimigo(inimigo4);
        verificarInimigo(inimigo5);

        if (GameSession.inimigosDerrotados >= 3) {
            desbloquearBoss();
        }
    }

    private void verificarBau(ImageView bau) {
        if (bau.isVisible() && player.getBoundsInParent().intersects(bau.getBoundsInParent())) {
            bau.setVisible(false);

            GameSession.moedasRun += 15;
            GameSession.pontosRun += 10;
            GameSession.addItem("Cristal", 1);

            msgLabel.setText("Você abriu um baú misterioso!");
            atualizarHud();
        }
    }

    private void verificarInimigo(ImageView inimigo) {
        if (inimigo.isVisible() && player.getBoundsInParent().intersects(inimigo.getBoundsInParent())) {
            abrirCombatMenu(inimigo);
        }
    }

    private void abrirCombatMenu(ImageView inimigo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/com/mycompany/undertale_3/CombatMenu.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Menu de Combate");

            CombatMenuController controller = loader.getController();
            controller.receberDados2(this, "Inimigo das Ruínas", stage);

            stage.setScene(new Scene(root));
            stage.show();

            inimigo.setVisible(false);
            GameSession.inimigosDerrotados++;

            if (GameSession.inimigosDerrotados >= 3) {
                desbloquearBoss();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void desbloquearBoss() {
        GameSession.bossDesbloqueado = true;
        bossPortal.setVisible(true);
        bossBtn.setDisable(false);
        msgLabel.setText("Boss desbloqueado! Entre no portal roxo.");
    }

    @FXML
    void abrirInventario() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/com/mycompany/undertale_3/inventario.fxml")
            );

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Inventário da run atual");

            Scene scene = new Scene(root, 420, 320);
            scene.getStylesheets().add(
                    App.class.getResource("/com/mycompany/undertale_3/style.css").toExternalForm()
            );

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirLoja() {
        pararMusica();
        App.trocarTela("loja.fxml", "Loja");
    }

    @FXML
    void abrirUpgrades() {
        pararMusica();
        App.trocarTela("upgrades.fxml", "Upgrades");
    }

    @FXML
    void abrirBoss() {
        if (GameSession.bossDesbloqueado) {
            pararMusica();
            App.trocarTela("boss.fxml", "Boss Final");
        }
    }

    @FXML
    void desistir() {
        pararMusica();
        fimRun(false);
    }

    private void fimRun(boolean venceu) {
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

        App.trocarTela("ranking.fxml", venceu ? "Vitória" : "Fim de Run");
    }

    private void atualizarHud() {
        nomeLabel.setText(GameSession.jogador.getNome());
        hpLabel.setText("HP: " + GameSession.hp + "/" + GameSession.maxHp);
        moedasLabel.setText("Moedas: " + GameSession.moedasRun);
        pontosLabel.setText("Pontos: " + GameSession.pontosRun);
    }

    public void abrirFight(String nomeInimigo, Stage menuStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/com/mycompany/undertale_3/Fight.fxml")
            );

            Parent root = loader.load();

            Stage fightStage = new Stage();
            fightStage.setTitle("Luta contra " + nomeInimigo);

            FightController controller = loader.getController();
            controller.receberDados2(this, nomeInimigo, fightStage);

            fightStage.setScene(new Scene(root));
            fightStage.show();

            if (menuStage != null) {
                menuStage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ganharXP(int xp) {
        GameSession.pontosRun += xp;
        atualizarHud();
    }

    public void ganharMoedas(int moedas) {
        GameSession.moedasRun += moedas;
        atualizarHud();
    }

    public boolean jogadorMorreu() {
        return GameSession.hp <= 0;
    }

    public void abrirGameOver() {
        pararMusica();
        App.trocarTela("GameOver.fxml", "GAME OVER");
    }

    public void levarDano(int dano) {
        if (GameSession.hp <= 0) return;

        GameSession.hp -= dano;

        if (GameSession.hp <= 0) {
            GameSession.hp = 0;
            atualizarHud();
            abrirGameOver();
            return;
        }

        atualizarHud();
    }

    public void adicionarItem(String item) {
        GameSession.addItem(item, 1);
        msgLabel.setText("Item coletado: " + item);
    }
}

