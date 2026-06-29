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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class JogoController {

    private Media media;
    private MediaPlayer playerv;

    @FXML private Pane mapaPane;

    @FXML private Label nomeLabel;
    @FXML private Label faseLabel;
    @FXML private Label hpLabel;
    @FXML private Label moedasLabel;
    @FXML private Label pontosLabel;
    @FXML private Label msgLabel;

    @FXML private ImageView player;
    @FXML private ImageView inimigo1;
    @FXML private ImageView inimigo2;
    @FXML private ImageView inimigo3;
    @FXML private ImageView bau1;
    @FXML private ImageView bau2;
    @FXML private ImageView bossPortal;

    private int faseAtual = 1;
    private int inimigosDerrotadosNaFase = 0;
    private int totalInimigosNaFase = 3;
    private int inimigosRestantesParaNascer = 0;
    private boolean bossAberto = false;

    @FXML
    void initialize() {
        iniciarMusica();
            mapaPane.prefWidthProperty().bind(
            mapaPane.getParent().layoutBoundsProperty().map(bounds -> bounds.getWidth())
        );

        mapaPane.prefHeightProperty().bind(
            mapaPane.getParent().layoutBoundsProperty().map(bounds -> bounds.getHeight())
        );

        player.setImage(img("personagem.png"));
        inimigo1.setImage(img("inimigo.png"));
        inimigo2.setImage(img("inimigo.png"));
        inimigo3.setImage(img("inimigo.png"));
        bau1.setImage(img("bau.png"));
        bau2.setImage(img("bau.png"));
        bossPortal.setImage(img("portal.png"));

        carregarFase(GameSession.faseAtual);

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
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar música: " + e.getMessage());
        }
    }

    private Image img(String nome) {
        return new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/" + nome));
    }

    private void carregarFase(int fase) {
        faseAtual = fase;
        GameSession.faseAtual = fase;
        inimigosDerrotadosNaFase = 0;
        bossAberto = false;

        totalInimigosNaFase = switch (faseAtual) {
            case 1 -> 3;
            case 2 -> 4;
            case 3 -> 5;
            case 4 -> 6;
            case 5 -> 7;
            default -> 3;
        };

        inimigosRestantesParaNascer = totalInimigosNaFase;

        player.setLayoutX(120);
        player.setLayoutY(150);

        bau1.setVisible(true);
        bau2.setVisible(true);
        bossPortal.setVisible(false);

        nascerOndaDeInimigos();

        msgLabel.setText(
                faseAtual == 5
                        ? "Fase 5: derrote os inimigos para liberar o Boss final."
                        : "Fase " + faseAtual + ": derrote os inimigos para liberar a próxima fase."
        );

        atualizarHud();
    }

    private void nascerOndaDeInimigos() {
        esconderTodosInimigos();

        if (inimigosRestantesParaNascer <= 0) {
            return;
        }

        double tamanho = 65 + faseAtual * 8;

        configurarInimigo(inimigo1, 350, 170, tamanho);
        inimigosRestantesParaNascer--;

        if (inimigosRestantesParaNascer > 0) {
            configurarInimigo(inimigo2, 570, 320, tamanho);
            inimigosRestantesParaNascer--;
        }

        if (inimigosRestantesParaNascer > 0) {
            configurarInimigo(inimigo3, 720, 180, tamanho);
            inimigosRestantesParaNascer--;
        }
    }

    private void configurarInimigo(ImageView inimigo, double x, double y, double tamanho) {
        inimigo.setLayoutX(x);
        inimigo.setLayoutY(y);
        inimigo.setFitWidth(tamanho);
        inimigo.setFitHeight(tamanho);
        inimigo.setVisible(true);
    }

    private void esconderTodosInimigos() {
        inimigo1.setVisible(false);
        inimigo2.setVisible(false);
        inimigo3.setVisible(false);
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

        if (inimigosDerrotadosNaFase >= totalInimigosNaFase) {
            desbloquearPortal();
        } else if (!temInimigoVisivel() && inimigosRestantesParaNascer > 0) {
            nascerOndaDeInimigos();
            msgLabel.setText("Mais inimigos apareceram!");
        }

        verificarPortal();
    }

    private boolean temInimigoVisivel() {
        return inimigo1.isVisible() || inimigo2.isVisible() || inimigo3.isVisible();
    }

    private void verificarBau(ImageView bau) {
        if (bau.isVisible() && player.getBoundsInParent().intersects(bau.getBoundsInParent())) {
            bau.setVisible(false);

            int moedas = 20 + faseAtual * 20;
            int pontos = 15 + faseAtual * 25;

            GameSession.moedasRun += moedas;
            GameSession.pontosRun += pontos;
            GameSession.addItem("Cristal da Fase " + faseAtual, 1);

            msgLabel.setText("Baú aberto! +" + moedas + " moedas e +" + pontos + " pontos.");
            atualizarHud();
        }
    }

    private void verificarInimigo(ImageView inimigo) {
        if (inimigo.isVisible() && player.getBoundsInParent().intersects(inimigo.getBoundsInParent())) {
            inimigo.setVisible(false);
            inimigosDerrotadosNaFase++;
            GameSession.inimigosDerrotados++;

            abrirCombatMenu();

            if (inimigosDerrotadosNaFase >= totalInimigosNaFase) {
                desbloquearPortal();
            }
        }
    }

    private void abrirCombatMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("/com/mycompany/undertale_3/CombatMenu.fxml")
            );

            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 650);
            scene.getStylesheets().add(
                    App.class.getResource("/com/mycompany/undertale_3/style.css")
                            .toExternalForm()
            );

            Stage stage = new Stage();
            stage.setTitle("Menu de Combate");
            stage.setScene(scene);
            stage.setResizable(false);

            CombatMenuController controller = loader.getController();
            controller.receberDados(this, "Inimigo da Fase " + faseAtual, stage);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void desbloquearPortal() {
        bossPortal.setVisible(true);

        if (faseAtual == 5) {
            msgLabel.setText("Portal liberado! Entre para enfrentar o Boss final.");
        } else {
            msgLabel.setText("Portal liberado! Entre para ir para a próxima fase.");
        }
    }

    private void verificarPortal() {
        if (bossPortal.isVisible()
                && player.getBoundsInParent().intersects(bossPortal.getBoundsInParent())) {

            if (faseAtual == 5) {
                abrirBoss();
            } else {
                avancarFase();
            }
        }
    }

    private void avancarFase() {
        if (faseAtual >= 5) {
            return;
        }

        carregarFase(faseAtual + 1);
    }

    private void abrirBoss() {
        if (bossAberto) return;

        bossAberto = true;
        bossPortal.setVisible(false);

        try {
            App.trocarTela("Boss.fxml", "Boss Final");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        App.trocarTela("loja.fxml", "Loja");
    }

    @FXML
    void abrirUpgrades() {
        App.trocarTela("upgrades.fxml", "Upgrades");
    }

    @FXML
    void desistir() {
        fimRun(false);
    }

    private void fimRun(boolean venceu) {
        if (playerv != null) {
            playerv.stop();
            playerv.dispose();
        }

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
        faseLabel.setText("Fase: " + faseAtual);
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

            Scene scene = new Scene(root, 900, 650);
            scene.getStylesheets().add(
                    App.class.getResource("/com/mycompany/undertale_3/style.css")
                            .toExternalForm()
            );

            Stage fightStage = new Stage();
            fightStage.setTitle("Luta contra " + nomeInimigo);
            fightStage.setScene(scene);
            fightStage.setResizable(false);

            FightController controller = loader.getController();
            controller.receberDados(this, nomeInimigo, fightStage, faseAtual);

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

        private void pararMusica() {
        try {
            if (playerv != null) {
                playerv.stop();
                playerv.dispose();
                playerv = null;
            }
        } catch (Exception e) {
            System.out.println("Música não estava carregada corretamente.");
        }
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