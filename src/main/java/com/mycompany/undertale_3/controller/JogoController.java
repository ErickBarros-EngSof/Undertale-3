package com.mycompany.undertale_3.controller;

import javafx.scene.media.*;
import com.mycompany.undertale_3.App;
import com.mycompany.undertale_3.dao.JogadorDAO;
import com.mycompany.undertale_3.dao.RunDAO;
import com.mycompany.undertale_3.util.GameSession;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JogoController {

    private Media media;
    private MediaPlayer playerv;
    @FXML private Pane mapaPane;
    @FXML private Label nomeLabel, hpLabel, moedasLabel, pontosLabel, msgLabel;
    @FXML private ImageView player, inimigo1, inimigo2, inimigo3, bau1, bau2, bossPortal;
    @FXML private Button bossBtn;

    @FXML
    void initialize() {
        String caminho = getClass().getResource("/com/mycompany/undertale_3/Música 2 UNDERTALE Toby Fox.m4a").toString();
        media = new Media(caminho);         
        playerv = new MediaPlayer(media);      
        playerv.setCycleCount(MediaPlayer.INDEFINITE); 
        playerv.play();
        atualizarHud();

        player.setImage(img("personagem.png"));
        inimigo1.setImage(img("inimigo.png"));
        inimigo2.setImage(img("inimigo.png"));
        inimigo3.setImage(img("inimigo.png"));
        bau1.setImage(img("bau.png"));
        bau2.setImage(img("bau.png"));
        bossPortal.setImage(img("portal.png"));

        bossPortal.setVisible(false);
        bossBtn.setDisable(true);

        Platform.runLater(() -> mapaPane.requestFocus());
    }

    private Image img(String nome) {
        return new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/" + nome));
    }

    @FXML
    private void teclaPressionada(javafx.scene.input.KeyEvent event) {
        switch (event.getCode()) {
            case W -> mover(0, -18);
            case S -> mover(0, 18);
            case A -> mover(-18, 0);
            case D -> mover(18, 0);
            default -> { }
        }
    }

    private void mover(double dx, double dy) {
        player.setLayoutX(Math.max(20, Math.min(840, player.getLayoutX() + dx)));
        player.setLayoutY(Math.max(70, Math.min(500, player.getLayoutY() + dy)));
        verificarColisoes();
    }

    private void verificarColisoes() {
        verificarBau(bau1);
        verificarBau(bau2);

        verificarInimigo(inimigo1);
        verificarInimigo(inimigo2);
        verificarInimigo(inimigo3);

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
            msgLabel.setText("Você abriu uma bolsa misteriosa!");
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
        controller.receberDados(this, "Inimigo das Ruínas", stage);

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
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mycompany/undertale_3/inventario.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Inventário da run atual");

            Scene scene = new Scene(root, 420, 320);
            scene.getStylesheets().add(App.class.getResource("/com/mycompany/undertale_3/style.css").toExternalForm());

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML void abrirLoja() { App.trocarTela("loja.fxml", "Loja"); }
    @FXML void abrirUpgrades() { App.trocarTela("upgrades.fxml", "Upgrades"); }
    @FXML void abrirBoss() { if (GameSession.bossDesbloqueado) App.trocarTela("boss.fxml", "Boss Final"); }
    @FXML void desistir() { fimRun(false); }

    private void fimRun(boolean venceu) {
        new RunDAO().salvarRun(GameSession.jogador.getId(), GameSession.pontosRun, GameSession.moedasRun, venceu);
        new JogadorDAO().somarResultado(GameSession.jogador.getId(), GameSession.pontosRun, GameSession.moedasRun);
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
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mycompany/undertale_3/Fight.fxml"));
            Parent root = loader.load();

            Stage fightStage = new Stage();
            fightStage.setTitle("Luta contra " + nomeInimigo);

            FightController controller = loader.getController();
            controller.receberDados(this, nomeInimigo, fightStage);

            fightStage.setScene(new Scene(root));
            fightStage.show();

            if (menuStage != null) menuStage.close();

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
