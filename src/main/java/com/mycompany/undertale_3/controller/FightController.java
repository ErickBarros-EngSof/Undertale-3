package com.mycompany.undertale_3.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.Random;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;

public class FightController {

    @FXML private Pane fightPane;
    @FXML private ImageView player;
    @FXML private ImageView inimigo;
    @FXML private Label vidaInimigoLabel;

    private JogoController gameController;
    private Jogo2Controller game2Controller;
    private Stage stage;
    private int vidaInimigo = 100;
    private double velocidadePlayer = 10;
    private double velocidadeInimigo = 2;
    private double direcaoX = 1;
    private double direcaoY = 1;
    private final Random random = new Random();
    private AnimationTimer timer;
    private boolean batalhaEncerrada = false;
    private long ultimoDano = 0;

    public void setGameController(JogoController gameController) {
    this.gameController = gameController;
    }
    
    private Jogo2Controller jogo2Controller;

        public void receberDados2(Jogo2Controller jogo2Controller, String nomeInimigo, Stage stage) {
            this.jogo2Controller = jogo2Controller;
            this.inimigo = inimigo;
            this.stage = stage;
        }
    
    @FXML
    public void initialize() {
        player.setImage(new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/personagem.png")));
        inimigo.setImage(new Image(getClass().getResourceAsStream("/com/mycompany/undertale_3/inimigo.png")));
        Platform.runLater(() -> fightPane.requestFocus());
        atualizarVida();
        iniciarMovimentoInimigo();
    }

    public void receberDados(JogoController gameController, String nomeInimigo, Stage stage) {
        this.gameController = gameController;
        this.stage = stage;
    }
    

    @FXML
    private void moverPlayer(KeyEvent event) {
        if (batalhaEncerrada) return;
        switch (event.getCode()) {
            case W -> player.setLayoutY(player.getLayoutY() - velocidadePlayer);
            case S -> player.setLayoutY(player.getLayoutY() + velocidadePlayer);
            case A -> player.setLayoutX(player.getLayoutX() - velocidadePlayer);
            case D -> player.setLayoutX(player.getLayoutX() + velocidadePlayer);
            default -> { }
        }
        limitarPlayerNaTela();
        verificarDano();
    }

    @FXML
    private void atacar(MouseEvent event) {
        if (batalhaEncerrada || event.getButton() != MouseButton.PRIMARY) return;
        if (inimigo.getBoundsInParent().contains(event.getX(), event.getY())) {
            vidaInimigo -= 10;
            atualizarVida();
            if (vidaInimigo <= 0) derrotarInimigo();
        }
    }

    private void iniciarMovimentoInimigo() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long agora) {
                if (batalhaEncerrada || !inimigo.isVisible()) {
                    stop();
                    return;
                }
                moverInimigo();
                if (random.nextInt(100) < 2) {
                    direcaoX = random.nextDouble() * 2 - 1;
                    direcaoY = random.nextDouble() * 2 - 1;
                }
                verificarDano();
            }
        };
        timer.start();
    }

    private void moverInimigo() {
        inimigo.setLayoutX(inimigo.getLayoutX() + direcaoX * velocidadeInimigo);
        inimigo.setLayoutY(inimigo.getLayoutY() + direcaoY * velocidadeInimigo);
        if (inimigo.getLayoutX() <= 0 || inimigo.getLayoutX() >= fightPane.getWidth() - inimigo.getFitWidth()) direcaoX *= -1;
        if (inimigo.getLayoutY() <= 0 || inimigo.getLayoutY() >= fightPane.getHeight() - inimigo.getFitHeight()) direcaoY *= -1;
    }

    private void verificarDano() {
        if (batalhaEncerrada || !inimigo.isVisible()) return;

        if (player.getBoundsInParent().intersects(inimigo.getBoundsInParent())) {
            long agora = System.currentTimeMillis();

            if (agora - ultimoDano > 1000) {
                ultimoDano = agora;

                if (gameController != null) {
                    gameController.levarDano(5);

                    if (gameController.jogadorMorreu()) {
                        batalhaEncerrada = true;

                        if (timer != null) {
                            timer.stop();
                        }

                        gameController.abrirGameOver();

                        if (stage != null) {
                            stage.close();
                        }

                        return;
                    }
                }else if (game2Controller != null) {
                    game2Controller.levarDano(5);

                    if (game2Controller.jogadorMorreu()) {
                        batalhaEncerrada = true;

                        if (timer != null) {
                            timer.stop();
                        }

                        game2Controller.abrirGameOver();

                        if (stage != null) {
                            stage.close();
                        }

                        return;
                    }
                }
            }
        }
    }

    private void derrotarInimigo() {
        batalhaEncerrada = true;
        if (timer != null) timer.stop();
        inimigo.setVisible(false);
        vidaInimigoLabel.setText("Inimigo derrotado!");
        if (gameController != null) {
            gameController.ganharXP(25);
            gameController.ganharMoedas(20);
            gameController.adicionarItem("Fragmento de alma");
        }else if(game2Controller != null) {
            game2Controller.ganharXP(25);
            game2Controller.ganharMoedas(20);
            game2Controller.adicionarItem("Fragmento de alma");
        }
        
        
        
        if (stage != null) stage.close();
    }

    private void limitarPlayerNaTela() {
        player.setLayoutX(Math.max(0, Math.min(player.getLayoutX(), fightPane.getWidth() - player.getFitWidth())));
        player.setLayoutY(Math.max(0, Math.min(player.getLayoutY(), fightPane.getHeight() - player.getFitHeight())));
    }

    private void atualizarVida() {
        vidaInimigoLabel.setText("Vida do inimigo: " + vidaInimigo);
    }
}
